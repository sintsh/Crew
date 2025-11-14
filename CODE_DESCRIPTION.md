# Crew Management App - Detailed Code Description

## 📋 Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Project Structure](#project-structure)
3. [Data Flow](#data-flow)
4. [Key Components Explained](#key-components-explained)
5. [Complete Example Flow](#complete-example-flow)
6. [Database Schema](#database-schema)
7. [Dependency Injection](#dependency-injection)

---

## 🏗️ Architecture Overview

This Android application follows **MVVM (Model-View-ViewModel)** architecture combined with **Clean Architecture** principles. The code is organized into three main layers:

### Layer Structure:
```
┌─────────────────────────────────────┐
│         UI Layer (Presentation)     │  ← Fragments, Activities, ViewModels
├─────────────────────────────────────┤
│      Domain Layer (Business Logic)  │  ← Use Cases, Domain Entities, Repository Interfaces
├─────────────────────────────────────┤
│      Data Layer (Data Sources)      │  ← Repositories, DAOs, Database, Entities
└─────────────────────────────────────┘
```



### Key Technologies:
- **Room Database** - Local SQLite database
- **Hilt** - Dependency Injection
- **Kotlin Coroutines & Flow** - Asynchronous operations
- **Navigation Component** - Fragment navigation
- **ViewBinding** - View binding

---

## 📁 Project Structure

```
app/src/main/java/com/example/crew/
├── app/                          # Application & UI Layer
│   ├── CrewApp.kt               # Application entry point (Hilt setup)
│   ├── di/                      # Dependency Injection modules
│   │   ├── DatabaseModule.kt    # Provides Room database & DAOs
│   │   └── RepositoryModule.kt  # Binds repository implementations
│   └── ui/                      # User Interface
│       ├── activities/          # Activities
│       ├── fragments/           # Fragments (Login, Admin, Employee)
│       ├── viewmodels/          # ViewModels (business logic for UI)
│       ├── adapters/            # RecyclerView adapters
│       ├── dialogs/             # Dialog fragments
│       └── helpers/             # UI helper classes
│
├── domain/                      # Domain Layer (Business Logic)
│   ├── entities/               # Domain entities (pure Kotlin data classes)
│   ├── respositories/          # Repository interfaces (contracts)
│   └── usecases/               # Business use cases
│       ├── employee/           # Employee-related use cases
│       └── role/               # Role-related use cases
│
└── data/                        # Data Layer
    ├── datasources/            # Data sources
    │   └── local/              # Local database
    │       ├── database/       # Room database
    │       ├── dao/            # Data Access Objects
    │       └── entity/         # Room entities
    ├── repositories/           # Repository implementations
    └── model/                  # Data models
```

---

## 🔄 Data Flow

### How Data Moves Through the App:

```
User Action (Fragment)
    ↓
ViewModel (processes user input)
    ↓
Use Case (business logic validation)
    ↓
Repository Interface (domain contract)
    ↓
Repository Implementation (data layer)
    ↓
DAO (database operations)
    ↓
Room Database (SQLite)
    ↓
Flow/StateFlow (reactive data stream)
    ↓
ViewModel (updates UI state)
    ↓
Fragment (observes and displays)
```

---

## 🔧 Key Components Explained

### 1. **Application Entry Point** (`CrewApp.kt`)

```kotlin
@HiltAndroidApp
class CrewApp: Application()
```

**What it does:**
- Marks the application class for Hilt dependency injection
- Hilt generates code to set up the dependency injection graph
- This is the first class that runs when the app starts

**How it works:**
- `@HiltAndroidApp` annotation tells Hilt to generate the DI setup code
- Hilt automatically creates and manages all dependencies defined in modules

---

### 2. **Database Layer** (`CrewDatabase.kt`)

```kotlin
@Database(
    entities = [Employee::class, Role::class, EmployeeRoleCrossRef::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class CrewDatabase: RoomDatabase()
```

**What it does:**
- Defines the Room database schema
- Provides access to DAOs (Data Access Objects)
- Manages database versioning and migrations

**Key Points:**
- **Entities**: Three tables - `employees`, `roles`, `employee_role_cross_ref`
- **Version**: Database version (incremented when schema changes)
- **AutoMigration**: Automatically handles schema changes between versions
- **DAOs**: Abstract methods that return DAO interfaces

**Database Schema:**
- `Employee` table: Stores employee information (id, username, name, lastName, age)
- `Role` table: Stores roles (id, roleName)
- `EmployeeRoleCrossRef` table: Many-to-many relationship between employees and roles

---

### 3. **Data Access Objects (DAOs)**

#### Example: `EmployeeDao.kt`

```kotlin
@Dao
interface EmployeeDao {
    @Insert
    suspend fun insertEmployee(employee: Employee): Long
    
    @Query("SELECT * FROM employees LIMIT :limit OFFSET :offset")
    fun getAllEmployees(limit:Int, offset:Int): Flow<List<Employee>>
    
    @Query("DELETE FROM employees WHERE employeeId = :employeeId")
    suspend fun deleteEmployee(employeeId: Long)
}
```

**What it does:**
- Defines database operations (CRUD - Create, Read, Update, Delete)
- Uses Room annotations to convert Kotlin functions to SQL queries
- Returns `Flow` for reactive data streams

**Key Annotations:**
- `@Insert`: Inserts data into the table
- `@Query`: Custom SQL queries
- `@Update`: Updates existing records
- `@Delete`: Deletes records
- `suspend`: Marks functions as coroutines (async operations)
- `Flow`: Returns reactive stream that emits data when database changes

**How it works:**
- Room generates implementation code at compile time
- When you call `getAllEmployees()`, Room executes the SQL query
- If data changes, the Flow automatically emits new data to observers

---

### 4. **Room Entities**

#### Example: `Employee.kt`

```kotlin
@Entity(tableName = "employees", indices = [Index(value = ["username"], unique = true)])
data class Employee(
    @PrimaryKey(autoGenerate = true) val employeeId: Long = 0,
    val username: String,
    val name: String,
    val lastName: String,
    val age: Int
)
```

**What it does:**
- Represents a database table structure
- Maps Kotlin data class to SQLite table

**Key Annotations:**
- `@Entity`: Marks class as a database table
- `@PrimaryKey`: Defines primary key (auto-generated)
- `indices`: Creates database indexes for performance
- `unique = true`: Ensures username is unique

**Extension Function:**
```kotlin
fun Employee.toEmployeeDE() = EmployeeDE(employeeId, username, name, lastName, age)
```
- Converts data layer entity to domain entity (separation of concerns)

---

### 5. **Repository Pattern**

#### Repository Interface (Domain Layer): `EmployeeRepository.kt`

```kotlin
interface EmployeeRepository {
    suspend fun getAllEmployees(limit: Int, offset: Int): Flow<List<EmployeeDE>>
    suspend fun saveEmployee(employee: Employee)
    suspend fun deleteEmployee(employeeId: Long)
}
```

**What it does:**
- Defines contract for data operations
- Domain layer depends on interface, not implementation
- Allows easy testing (can mock the interface)

#### Repository Implementation (Data Layer): `EmployeeRepositoryImpl.kt`

```kotlin
class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao
): EmployeeRepository {
    override suspend fun getAllEmployees(limit: Int, offset: Int): Flow<List<EmployeeDE>> {
        return employeeDao.getAllEmployees(limit, offset).map { employees ->
            employees.map { employee -> employee.toEmployeeDE() }
        }
    }
}
```

**What it does:**
- Implements the repository interface
- Converts data entities to domain entities
- Acts as a bridge between domain and data layers

**Key Points:**
- Uses `@Inject` for dependency injection (receives DAO from Hilt)
- Maps Room entities to domain entities using extension functions
- Returns `Flow` for reactive data streams

---

### 6. **Use Cases (Business Logic)**

#### Example: `SaveEmployeeUseCase.kt`

```kotlin
class SaveEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): Result<EmployeeDE> {
        if (checkEmployeeSavable(employee)) {
            try {
                employeeRepository.saveEmployee(employee)
                return Result.Success(employee.toEmployeeDE())
            } catch (e: Exception) {
                return Result.Error(e.message.toString())
            }
        } else {
            return Result.Error("Employee is not savable")
        }
    }
    
    fun checkEmployeeSavable(employee: Employee): Boolean {
        return (employee.name.length > 3)
                && (employee.lastName.length > 3)
                && (employee.username.length > 3)
                && (employee.age > 18)
                && (employee.age < 70)
    }
}
```

**What it does:**
- Contains business logic for saving an employee
- Validates data before saving
- Returns `Result` wrapper (Success/Error/Loading)

**Key Points:**
- Single responsibility: One use case = one business operation
- Validation logic: Ensures data meets business rules
- Error handling: Catches exceptions and returns error result
- `operator fun invoke`: Allows calling use case like a function: `saveEmployeeUseCase(employee)`

---

### 7. **ViewModels**

#### Example: `AdminHomeViewModel.kt`

```kotlin
@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val saveEmployeeUseCase: SaveEmployeeUseCase,
    // ... other use cases
) : ViewModel() {
    
    private val _offset = MutableStateFlow(0)
    val offset = _offset.asStateFlow()
    
    val employeeWithRoles: Flow<List<EmployeeWithRolesDE>> = combine(
        offset, limit, _searchQueries, dataUpdateTrigger
    ) { currentOffset, currentLimit, currentSearchQuery, _ ->
        Pair(currentOffset, currentLimit)
    }.flatMapLatest { (currentOffset, currentLimit) ->
        getEmployeeWithRoles()
    }
    
    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            saveEmployeeUseCase(employee)
            fetchEmployeeCount()
        }
    }
}
```

**What it does:**
- Manages UI-related data and business logic
- Exposes data to UI through StateFlow/Flow
- Handles user actions and calls use cases
- Survives configuration changes (screen rotation)

**Key Concepts:**

1. **StateFlow**: Mutable state holder that UI can observe
   - `_offset` (private, mutable) - internal state
   - `offset` (public, read-only) - exposed to UI

2. **Flow Operators:**
   - `combine`: Combines multiple flows into one
   - `flatMapLatest`: Transforms flow and cancels previous if new value arrives
   - `debounce`: Waits for pause in input (used for search)

3. **viewModelScope**: Coroutine scope that cancels when ViewModel is cleared

4. **Reactive Data:**
   - When `offset` changes → `employeeWithRoles` flow automatically updates
   - UI observes this flow and updates automatically

---

### 8. **Fragments (UI Layer)**

#### Example: `AdminHomeFragment.kt`

```kotlin
@AndroidEntryPoint
class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {
    
    private val viewModel: AdminHomeViewModel by viewModels()
    private lateinit var employeeAdapter: EmployeeListRecyclerAdapter
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModelState()
    }
    
    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.employeeWithRoles.collectLatest { employees ->
                    employeeAdapter.submitList(employees)
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.createButton.setOnClickListener {
            // Navigate to create employee dialog
        }
        
        binding.deleteAllButton.setOnClickListener { 
            viewModel.deleteAllEmployees() 
        }
    }
}
```

**What it does:**
- Displays UI and handles user interactions
- Observes ViewModel state and updates UI
- Calls ViewModel methods when user performs actions

**Key Points:**

1. **@AndroidEntryPoint**: Enables Hilt injection in fragment

2. **by viewModels()**: Creates ViewModel instance (managed by Hilt)

3. **Lifecycle-aware Collection:**
   ```kotlin
   repeatOnLifecycle(Lifecycle.State.STARTED) {
       viewModel.employeeWithRoles.collectLatest { employees ->
           // Update UI
       }
   }
   ```
   - Only collects data when fragment is in STARTED state
   - Automatically cancels collection when fragment stops
   - Prevents memory leaks

4. **collectLatest**: Cancels previous collection when new data arrives

5. **User Actions:**
   - Button clicks → Call ViewModel methods
   - ViewModel processes → Updates state
   - State change → Flow emits → Fragment updates UI

---

### 9. **Dependency Injection (Hilt)**

#### Database Module: `DatabaseModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): CrewDatabase =
        Room.databaseBuilder(app, CrewDatabase::class.java, "crew_db.db").build()
    
    @Provides
    fun provideEmployeeDao(db: CrewDatabase): EmployeeDao = db.employeeDao()
}
```

**What it does:**
- Provides database and DAO instances
- Singleton: One database instance for entire app
- Hilt automatically injects these where needed

#### Repository Module: `RepositoryModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindEmployeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository
}
```

**What it does:**
- Binds repository implementations to interfaces
- When code requests `EmployeeRepository`, Hilt provides `EmployeeRepositoryImpl`

**How DI Works:**
1. Hilt scans classes with `@Inject` constructor
2. Finds required dependencies in modules
3. Creates dependency graph
4. Injects dependencies automatically

**Example:**
```kotlin
// ViewModel needs UseCase
@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val saveEmployeeUseCase: SaveEmployeeUseCase  // Hilt provides this
)

// UseCase needs Repository
class SaveEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository  // Hilt provides this
)

// Repository needs DAO
class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao  // Hilt provides this
)
```

---

## 🔄 Complete Example Flow: Adding an Employee

Let's trace what happens when a user adds a new employee:

### Step 1: User Clicks "Create Employee" Button

```kotlin
// AdminHomeFragment.kt
binding.createButton.setOnClickListener {
    val direction = AdminMainFragmentDirections
        .actionAdminMainFragmentToEmployeeActionDialogFragment(
            null, 
            actionType = ActionType.CREATE
        )
    findNavController().navigate(direction)
}
```

**What happens:**
- Navigation Component navigates to `EmployeeActionDialogFragment`
- User fills in employee details and clicks "Save"

---

### Step 2: Dialog Returns Data to Fragment

```kotlin
// AdminHomeFragment.kt
findNavController().currentBackStackEntry
    ?.savedStateHandle
    ?.getLiveData<EmployeeWithAction>("employee_with_action_result")
    ?.observe(viewLifecycleOwner) { employeeWithAction ->
        if (employeeWithAction.action == ActionType.CREATE) {
            viewModel.addEmployee(employeeWithAction.employee.toEmployee())
        }
    }
```

**What happens:**
- Dialog saves result to navigation saved state
- Fragment observes and receives the data
- Calls ViewModel's `addEmployee()` method

---

### Step 3: ViewModel Processes Request

```kotlin
// AdminHomeViewModel.kt
fun addEmployee(employee: Employee) {
    viewModelScope.launch {
        saveEmployeeUseCase(employee)
        fetchEmployeeCount()
    }
}
```

**What happens:**
- Launches coroutine in ViewModel scope
- Calls `SaveEmployeeUseCase`
- Updates employee count after saving

---

### Step 4: Use Case Validates and Saves

```kotlin
// SaveEmployeeUseCase.kt
suspend operator fun invoke(employee: Employee): Result<EmployeeDE> {
    if (checkEmployeeSavable(employee)) {  // Validation
        try {
            employeeRepository.saveEmployee(employee)  // Save to database
            return Result.Success(employee.toEmployeeDE())
        } catch (e: Exception) {
            return Result.Error(e.message.toString())
        }
    } else {
        return Result.Error("Employee is not savable")
    }
}
```

**What happens:**
- Validates employee data (name > 3 chars, age 18-70, etc.)
- If valid, calls repository to save
- Returns Success or Error result

---

### Step 5: Repository Saves to Database

```kotlin
// EmployeeRepositoryImpl.kt
override suspend fun saveEmployee(employee: Employee) {
    employeeDao.insertEmployee(employee)
}
```

**What happens:**
- Repository calls DAO's insert method
- Converts domain entity to data entity if needed

---

### Step 6: DAO Inserts into Database

```kotlin
// EmployeeDao.kt
@Insert
suspend fun insertEmployee(employee: Employee): Long
```

**What happens:**
- Room executes SQL INSERT statement
- Returns generated employee ID
- Database is updated

---

### Step 7: Flow Automatically Updates

```kotlin
// EmployeeDao.kt
@Query("SELECT * FROM employees LIMIT :limit OFFSET :offset")
fun getAllEmployees(limit:Int, offset:Int): Flow<List<Employee>>
```

**What happens:**
- Room detects database change
- Flow automatically emits new data
- All observers receive updated employee list

---

### Step 8: ViewModel Receives Update

```kotlin
// AdminHomeViewModel.kt
val employeeWithRoles: Flow<List<EmployeeWithRolesDE>> = combine(
    offset, limit, _searchQueries, dataUpdateTrigger
).flatMapLatest { (currentOffset, currentLimit) ->
    getEmployeeWithRoles()  // This flow emits new data
}
```

**What happens:**
- Flow in ViewModel receives new data
- Combines with other flows (pagination, search)
- Emits updated list

---

### Step 9: Fragment Updates UI

```kotlin
// AdminHomeFragment.kt
viewModel.employeeWithRoles.collectLatest { employees ->
    employeeAdapter.submitList(employees)  // Update RecyclerView
}
```

**What happens:**
- Fragment collects latest data from ViewModel
- Updates RecyclerView adapter
- UI displays new employee in the list

---

## 🗄️ Database Schema

### Tables:

1. **employees**
   ```
   employeeId (PK, Auto) | username (Unique) | name | lastName | age
   ```

2. **roles**
   ```
   roleId (PK, Auto) | roleName
   ```

3. **employee_role_cross_ref** (Many-to-Many)
   ```
   employeeId (FK) | roleId (FK)
   ```

### Relationships:
- **Employee ↔ Role**: Many-to-Many
  - One employee can have multiple roles
  - One role can be assigned to multiple employees
  - Junction table: `EmployeeRoleCrossRef`

### Foreign Keys:
- `EmployeeRoleCrossRef.employeeId` → `Employee.employeeId` (CASCADE DELETE)
- `EmployeeRoleCrossRef.roleId` → `Role.roleId` (CASCADE DELETE)

**CASCADE DELETE**: When an employee is deleted, all their role assignments are automatically deleted.

---

## 🔐 Login Flow

### How Login Works:

1. **User enters username and password** → `LoginFragment`

2. **Fragment validates input** → Calls `loginViewModel.loginDataChanged()`
   ```kotlin
   fun loginDataChanged(username: String, password: String) {
       if (!isUserNameValid(username)) {
           _loginForm.value = LoginFormState(usernameError = ...)
       } else if (!isPasswordValid(password)) {
           _loginForm.value = LoginFormState(passwordError = ...)
       }
   }
   ```

3. **User clicks login** → Calls `loginViewModel.login(username, password)`

4. **ViewModel fetches employee** → Uses `GetEmployeeWithRolesByUserNameUseCase`

5. **Checks for admin role**:
   ```kotlin
   val adminRole = employeeResult.data.roles.any {
       it.roleName == "admin"
   }
   ```

6. **Navigates based on role**:
   - Admin → `AdminMainFragment`
   - Normal → `EmployeeFragment`

---

## 📊 State Management

### StateFlow vs Flow:

- **StateFlow**: Holds current state, always has a value
  ```kotlin
  private val _offset = MutableStateFlow(0)  // Initial value: 0
  val offset = _offset.asStateFlow()  // Read-only
  ```

- **Flow**: Stream of values, no initial value required
  ```kotlin
  val employeeList: Flow<List<EmployeeDE>> = getEmployeesUseCase()
  ```

### Reactive Updates:

When `_offset.value++` is called:
1. StateFlow emits new value
2. `combine()` operator detects change
3. `flatMapLatest()` cancels old flow, starts new one
4. New data is fetched
5. Flow emits to observers
6. UI updates automatically

---

## 🎯 Key Design Patterns

1. **MVVM**: Separation of UI (Fragment) and logic (ViewModel)

2. **Repository Pattern**: Abstracts data source from business logic

3. **Use Case Pattern**: Single responsibility for each business operation

4. **Dependency Injection**: Loose coupling, easy testing

5. **Observer Pattern**: Flow/StateFlow for reactive updates

6. **Clean Architecture**: Separation into layers (UI, Domain, Data)

---

## 🧪 Testing Strategy

- **Unit Tests**: Test use cases, repositories (in `test/` folder)
- **Integration Tests**: Test ViewModels with mocked repositories
- **UI Tests**: Test fragments with Hilt test setup (in `androidTest/` folder)

---

## 🚀 Summary

This app demonstrates:
- ✅ Clean Architecture with clear layer separation
- ✅ MVVM pattern for UI logic
- ✅ Reactive programming with Kotlin Flow
- ✅ Dependency Injection with Hilt
- ✅ Room database for local storage
- ✅ Navigation Component for screen navigation
- ✅ Coroutines for asynchronous operations

The code follows Android best practices and is maintainable, testable, and scalable.


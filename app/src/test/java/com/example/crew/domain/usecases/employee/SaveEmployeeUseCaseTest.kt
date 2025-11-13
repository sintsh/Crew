package com.example.crew.domain.usecases.employee
import com.example.crew.data.Result
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.respositories.EmployeeRepository
import com.example.crew.domain.respositories.RoleRepository
import com.example.crew.domain.usecases.role.GetAllRolesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SaveEmployeeUseCaseTest {
    @MockK
    private lateinit var employeeRepository: EmployeeRepository

    @MockK
    private lateinit var roleRepository: RoleRepository

    private lateinit var saveEmployeeUseCase: SaveEmployeeUseCase

    private lateinit var getAllRolesUseCase: GetAllRolesUseCase


    @BeforeEach
    fun setUpClass() {
        saveEmployeeUseCase = SaveEmployeeUseCase(employeeRepository)
        getAllRolesUseCase = GetAllRolesUseCase(roleRepository)

        coEvery { getAllRolesUseCase() } returns flowOf(listOf<RolesDE>(RolesDE(1, "admin")))

    }



    @Test
    fun `checkEmployeeSavable should return true for employee older than 18`() {
        // Arrange
        val employee = Employee(1, "johny", "John", "Jacobson", 19)

        // Act
        val result = saveEmployeeUseCase.checkEmployeeSavable(employee)

        // Assert
        Assertions.assertTrue(result)
    }

    @Test
    fun `checkEmployeeSavable should return false for employee aged 18 or younger`() {
        // Arrange
        val employee = Employee(1, "johny", "John", "Jacobson", 18)

        // Act
        val result = saveEmployeeUseCase.checkEmployeeSavable(employee)

        // Assert
        Assertions.assertFalse(result)
    }


    @Test
    fun `invoke should call repository's saveEmployee when employee is valid`() =
        runBlocking {
        // Arrange
        val validEmployee = Employee(1, "johny", "John", "Jacobson", 25)
        coEvery { employeeRepository.saveEmployee(validEmployee) } just runs

        saveEmployeeUseCase.invoke(validEmployee)

            coVerify(exactly = 1) {
                employeeRepository.saveEmployee(validEmployee)
            }
    }

    @Test
    fun `invoke should throw exception and not call repository when employee is invalid`() =
        runBlocking {
        // Arrange
        val invalidEmployee = Employee(1, "johny", "John", "Jacobson", 17)

            runBlocking {
                saveEmployeeUseCase.invoke(invalidEmployee)
            }

        coVerify(exactly = 0) {
            employeeRepository.saveEmployee(any())
        }
    }

    @Test
    fun `getAllRoles should return list of roles when data is available`(){
        runBlocking {
            val roles: Flow<List<RolesDE>> = getAllRolesUseCase()


            roles.collectLatest { roles->
                print("roles : $roles")
                assert(roles.isNotEmpty())
            }
        }
    }

}
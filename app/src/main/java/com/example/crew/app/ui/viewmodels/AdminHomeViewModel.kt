package com.example.crew.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.entities.EmployeeWithRolesDE
import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.usecases.employee.DeleteAllEmployeesUseCase
import com.example.crew.domain.usecases.employee.DeleteEmployeeUseCase
import com.example.crew.domain.usecases.employee.GetEmployeeWithRoles
import com.example.crew.domain.usecases.employee.GetEmployeesUseCase
import com.example.crew.domain.usecases.employee.SaveEmployeeUseCase
import com.example.crew.domain.usecases.employee.SearchEmployeeUseCase
import com.example.crew.domain.usecases.employee.UpdateEmployeeUseCase
import com.example.crew.domain.usecases.role.AddEmployeeRoleUseCase
import com.example.crew.domain.usecases.role.DeleteEmployeeRoleUseCase
import com.example.crew.domain.usecases.role.GetAllRolesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val saveEmployeeUseCase: SaveEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val deleteAllEmployeesUseCase: DeleteAllEmployeesUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase,
    private val searchEmployeeUseCase: SearchEmployeeUseCase,
    private val getEmployeeWithRoles: GetEmployeeWithRoles,
    private val getAllRolesUseCase: GetAllRolesUseCase,
    private val addEmployeeRoleUseCase: AddEmployeeRoleUseCase,
    private val deleteEmployeeRoleUseCase: DeleteEmployeeRoleUseCase
) : ViewModel() {

    private val _limit = MutableStateFlow(6)
    val limit = _limit.asStateFlow()

    private val _offset = MutableStateFlow(0)
    val offset = _offset.asStateFlow()
    private val _maxEmployeeCount = MutableStateFlow(0)
    val maxEmployeeCount = _maxEmployeeCount.asStateFlow()

    private val _roles = MutableStateFlow<List<RolesDE>>(listOf())
    val roles = _roles.asStateFlow()

    private val _searchQueries = MutableStateFlow("")
    val searchQueries = _searchQueries.asStateFlow()

    val hasNextPage: Flow<Boolean> =
        combine(offset, limit, maxEmployeeCount) { currentOffset, currentLimit, maxCount ->
            (currentOffset + 1) * currentLimit < maxCount
        }

    val hasPreviousPage: Flow<Boolean> = offset.combine(limit) { currentOffset, _ ->
        currentOffset > 0
    }

    private val dataUpdateTrigger = MutableStateFlow(0)
//    var employeeList: Flow<List<EmployeeDE>> = combine(
//        offset,
//        limit,
//        _searchQueries.debounce(600),
//        dataUpdateTrigger,
//    ) { currentOffset, currentLimit, searchQueries,_ ->
//        Pair(currentOffset, currentLimit)
//    }.flatMapLatest { (currentOffset, currentLimit) ->
//        val dbOffset = currentOffset * currentLimit
//        if (_searchQueries.value.isNotBlank()){
//            searchEmployeeUseCase(_searchQueries.value).map {
//                it
//            }
//        }else{
//            getEmployeesUseCase(currentLimit, dbOffset)
//        }
//    }

    val employeeWithRoles: Flow<List<EmployeeWithRolesDE>> = combine(
        offset, limit, _searchQueries, dataUpdateTrigger
    ){ currentOffset, currentLimit, currentSearchQuery, _ ->
        Pair(currentOffset, currentLimit)
    }.flatMapLatest { (currentOffset, currentLimit)->
        //val dbOffset = currentOffset * currentLimit
        getEmployeeWithRoles()
    }

    init {
        fetchEmployeeCount()
        getRoles()
    }


    fun getRoles(){
        viewModelScope.launch {
            getAllRolesUseCase().collectLatest { rolesDES ->
                _roles.value = rolesDES
            }
        }
    }

    fun addEmployeeRole(employeeId:Long,selected:List<Int>){
        viewModelScope.launch {
            for (select in selected){
                addEmployeeRoleUseCase(EmployeeRoleCrossRef(employeeId, select.toLong()))
            }        }
    }

    fun deleteEmployeeRole(employeeId: Long, unselected: List<Long>){
        viewModelScope.launch {
            for (unselect in unselected){
                deleteEmployeeRoleUseCase(EmployeeRoleCrossRef(employeeId, unselect.toLong()))
            }
        }
    }

    fun setQuery(query:String){
        _searchQueries.value = query
    }

    fun onNextPage() {
        val canMove = ((_offset.value + 1) * _limit.value) < _maxEmployeeCount.value
        if (canMove) {
            _offset.value++
        }
    }

    fun onPreviousPage() {
        if (_offset.value > 0) {
            _offset.value--
        }
    }

    fun changeLimit(newLimit: Int) {
        _limit.value = newLimit
        _offset.value = 0
    }

    private fun fetchEmployeeCount() {
        viewModelScope.launch {
            _maxEmployeeCount.value = getEmployeesUseCase.getEmployeeCount()
        }
    }

    fun addEmployee(employee: Employee= Employee(username = "test", name = "test", lastName = "test", age = 20)){
        viewModelScope.launch {
            saveEmployeeUseCase(employee)
            fetchEmployeeCount()
        }
    }

    fun updateEmployee(employeeId: Long,username:String, name:String, lastName:String, age:Int){
        viewModelScope.launch {
            updateEmployeeUseCase(employeeId,username, name, lastName, age)
        }
    }




    fun deleteEmployee(employeeId:Long){
        viewModelScope.launch {
            deleteEmployeeUseCase(employeeId)
            fetchEmployeeCount()

            if ((_maxEmployeeCount.value%_limit.value)==1){
                if (_offset.value>0){
                    _offset.value = _offset.value -1
                }
            }
        }

    }

    fun deleteAllEmployees(){
        viewModelScope.launch {
            deleteAllEmployeesUseCase()
            fetchEmployeeCount()
        }
        _offset.value = 0
    }

    suspend fun getEmployeeById(employeeId: Long): Flow<EmployeeDE?>{
            return getEmployeesUseCase.getById(employeeId)
    }
}
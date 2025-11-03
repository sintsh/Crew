package com.example.crew.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.usecases.DeleteAllEmployeesUseCase
import com.example.crew.domain.usecases.DeleteEmployeeUseCase
import com.example.crew.domain.usecases.GetEmployeesUseCase
import com.example.crew.domain.usecases.SaveEmployeeUseCase
import com.example.crew.domain.usecases.UpdateEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val saveEmployeeUseCase: SaveEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val deleteAllEmployeesUseCase: DeleteAllEmployeesUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase
) : ViewModel() {

    private val _limit = MutableStateFlow(6)
    val limit = _limit.asStateFlow()

    private val _offset = MutableStateFlow(0)
    val offset = _offset.asStateFlow()
    private val _maxEmployeeCount = MutableStateFlow(0)
    val maxEmployeeCount = _maxEmployeeCount.asStateFlow()



    val hasNextPage: Flow<Boolean> =
        combine(offset, limit, maxEmployeeCount) { currentOffset, currentLimit, maxCount ->
            (currentOffset + 1) * currentLimit < maxCount
        }

    val hasPreviousPage: Flow<Boolean> = offset.combine(limit) { currentOffset, _ ->
        currentOffset > 0
    }

    private val dataUpdateTrigger = MutableStateFlow(0)
    val employeeList: Flow<List<EmployeeDE>> = combine(
        offset,
        limit,
        dataUpdateTrigger
    ) { currentOffset, currentLimit, _ ->
        Pair(currentOffset, currentLimit)
    }.flatMapLatest { (currentOffset, currentLimit) ->
        val dbOffset = currentOffset * currentLimit
        getEmployeesUseCase(currentLimit, dbOffset)
    }


    init {
        fetchEmployeeCount()
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

    fun addEmployee(employee: Employee){
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
        }

    }

    fun deleteAllEmployees(){
        viewModelScope.launch {
            deleteAllEmployeesUseCase()
            fetchEmployeeCount()
        }
    }

    suspend fun getEmployeeById(employeeId: Long): Flow<EmployeeDE>{
            return getEmployeesUseCase.getById(employeeId)
    }
}
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val saveEmployeeUseCase: SaveEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val deleteAllEmployeesUseCase: DeleteAllEmployeesUseCase
) : ViewModel() {

    private var _employeeList = MutableStateFlow<List<EmployeeDE>>(listOf())

    val employeeList = _employeeList.asStateFlow()


    init {
        Log.i("Crewhere", ": initialized")
        viewModelScope.launch {
            getEmployeesUseCase().collectLatest { employees->
                Log.i("Crewhere", ": gotten $employees")

                _employeeList.value = employees
            }
        }
    }


    fun addEmployee(employee: Employee){
        viewModelScope.launch {
            saveEmployeeUseCase(employee)
        }
    }


    fun deleteEmployee(employeeId:Long){
        viewModelScope.launch {
            deleteEmployeeUseCase(employeeId)
        }
    }

    fun deleteAllEmployees(){
        viewModelScope.launch {
            deleteAllEmployeesUseCase()
        }
    }


    suspend fun getEmployeeById(employeeId: Long): Flow<EmployeeDE>{
            return getEmployeesUseCase.getById(employeeId)
    }

//    fun generateRandomAlphanumericString(length: Int): String {
////        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
////        return CharArray(length) { allowedChars.random(Random.Default) }.concatToString()
//    }

//    fun generateId(): Long {
////        return _employeeList.value.last().employeeId+1
//    }
}
package com.example.crew.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.crew.data.datasources.local.entity.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class AdminHomeViewModel : ViewModel() {

    private var _employeeList = MutableStateFlow< List<Employee>>(
        mutableListOf(
            Employee(1, "someone","John","Doe",29))
    )

    val employeeList = _employeeList.asStateFlow()


    fun addEmployee(){
        _employeeList.value.sortedBy {
            it.employeeId
        }
        _employeeList.value = listOf<Employee>(Employee(generateId(), generateRandomAlphanumericString(5),generateRandomAlphanumericString(4),"Doe",29))
    }


    fun deleteEmployee(employeeId:Long){
        _employeeList.value.filterNot {it.employeeId==employeeId}
    }

    fun deleteAllEmployees(){
        _employeeList.value = mutableListOf<Employee>()
    }

    fun generateRandomAlphanumericString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return CharArray(length) { allowedChars.random(Random.Default) }.concatToString()
    }

    fun generateId(): Long {
        return _employeeList.value.last().employeeId+1
    }
}
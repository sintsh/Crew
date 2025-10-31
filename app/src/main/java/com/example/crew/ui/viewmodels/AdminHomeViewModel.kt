package com.example.crew.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.crew.data.datasources.local.entity.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class AdminHomeViewModel : ViewModel() {

    private val _employeeList = MutableStateFlow<List<Employee>>(
        mutableListOf(
            Employee(1, "someone","John","Doe",29))
    )

    val employeeList = _employeeList.asStateFlow()


    fun addEmployee(){
        _employeeList.value = _employeeList.value + mutableListOf(

                Employee(1, generateRandomAlphanumericString(5),generateRandomAlphanumericString(4),"Doe",29),
                Employee(1, generateRandomAlphanumericString(5),generateRandomAlphanumericString(4),"Smith",29)

        )
    }



    fun generateRandomAlphanumericString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return CharArray(length) { allowedChars.random(Random.Default) }.concatToString()
    }
}
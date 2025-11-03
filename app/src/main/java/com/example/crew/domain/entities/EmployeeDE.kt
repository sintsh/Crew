package com.example.crew.domain.entities

import android.os.Parcelable
import com.example.crew.data.datasources.local.entity.Employee
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeDE(
    val employeeId: Long? = 0,
    val username: String,
    val name:String,
    val lastName:String,
    val age:Int,
): Parcelable


fun EmployeeDE.toEmployee() = Employee(username = username, name = name, lastName = lastName, age = age)

package com.example.crew.data.datasources.local.entity

import androidx.room.TypeConverters

@TypeConverters(EmployeeConverter::class)
data class PagedEmployee(
    val employeeList: List<Employee>,
    val employeeCount:Int
)

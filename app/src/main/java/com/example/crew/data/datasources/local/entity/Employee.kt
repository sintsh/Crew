package com.example.crew.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.crew.domain.entities.EmployeeDE

@Entity(tableName = "employees", indices = [Index(value = ["username"], unique = true)])
data class Employee(
    @PrimaryKey(autoGenerate = true) val employeeId: Long = 0,

    val username: String,
    val name:String,
    val lastName:String,
    val age:Int,

    )


fun Employee.toEmployeeDE() =
    EmployeeDE(employeeId,username, name, lastName, age)
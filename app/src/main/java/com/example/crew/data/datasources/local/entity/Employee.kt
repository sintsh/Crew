package com.example.crew.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val employeeId: Long = 0,
    val username: String,
    val name:String,
    val lastName:String,
    val age:Int,

    )

package com.example.crew.data.datasources.local.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EmployeeConverter {
    @TypeConverter
    fun fromEmployeeList(employeeList: List<Employee>):String{
        return Gson().toJson(employeeList)
    }

    @TypeConverter
    fun toEmployeeList(employeeJson: String):List<Employee>{
        val type = object : TypeToken<List<Employee>>() {}.type
        return Gson().fromJson(employeeJson, type)
    }
}
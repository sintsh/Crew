package com.example.crew.domain.respositories

import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.entities.EmployeeDE
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    suspend fun getAllEmployees(limit:Int, offset: Int): Flow<List<EmployeeDE>>
    suspend fun saveEmployee(employee: Employee)

    suspend fun deleteEmployee(employeeId: Long)

    suspend fun deleteAllEmployees()

    suspend fun getEmployeeById(employeeId: Long): Employee

    suspend fun getEmployeeCount(): Int

    suspend fun updateEmployee(employeeId:Long,username:String, name:String, lastName:String, age:Int)
}
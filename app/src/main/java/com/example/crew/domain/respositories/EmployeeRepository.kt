package com.example.crew.domain.respositories

import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.entities.EmployeeDE
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    suspend fun getAllEmployees(): Flow<List<EmployeeDE>>
    suspend fun saveEmployee(employee: Employee)

    suspend fun deleteEmployee(employeeId: Long)
}
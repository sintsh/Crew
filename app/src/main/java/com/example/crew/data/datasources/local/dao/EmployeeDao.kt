package com.example.crew.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.crew.data.datasources.local.entity.Employee
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insertEmployee(employee: Employee): Long

    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<Employee>>

    @Transaction
    @Query("SELECT * FROM employees WHERE employeeId = :employeeId")
    fun getEmployeesByRole(employeeId: Long): Flow<List<Employee>>

    @Query("DELETE FROM employees WHERE employeeId = :employeeId")
    suspend fun deleteEmployee(employeeId: Long)

}
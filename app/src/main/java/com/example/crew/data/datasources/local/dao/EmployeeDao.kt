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

    @Query("SELECT * FROM employees  LIMIT :limit OFFSET :offset")
    fun getAllEmployees(limit:Int, offset:Int): Flow<List<Employee>>

    @Transaction
    @Query("SELECT * FROM employees WHERE employeeId = :employeeId")
    fun getEmployeesByRole(employeeId: Long): Flow<List<Employee>>

    @Query("DELETE FROM employees WHERE employeeId = :employeeId")
    suspend fun deleteEmployee(employeeId: Long)

    @Query("DELETE FROM employees")
    suspend fun deleteAllEmployees()

    @Query("SELECT * FROM employees WHERE employeeId = :employeeId")
    suspend fun getEmployeeById(employeeId: Long): Employee

    @Query("SELECT COUNT(*) FROM employees")
    suspend fun getEmployeeCount():Int

    @Query("UPDATE employees SET username = :username, name = :name, lastname = :lastName, age = :age WHERE employeeId = :employeeId")
    suspend fun updateEmployee(employeeId: Long, username: String, name:String, lastName:String, age:Int)

}
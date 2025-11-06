package com.example.crew.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.data.datasources.local.entity.EmployeeWithRoles
import com.example.crew.data.datasources.local.entity.RolesWithEmployee
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeRoleCrossRefDao {
    @Insert
    suspend fun insertUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)

    @Transaction
    @Query("SELECT * FROM employees")
    fun getEmployeesWithRoles(): Flow<List<EmployeeWithRoles>>

    @Transaction
    @Query("SELECT * FROM employees WHERE username = :username")
    suspend fun getEmployeesWithRolesByUserName(username: String): EmployeeWithRoles?

    @Transaction
    @Query("SELECT * FROM roles")
    fun getRoleWithEmployees(): Flow<List<RolesWithEmployee>>

    @Transaction
    @Query("SELECT * FROM roles WHERE roleName = :roleName")
    fun getEmployeesByRoleName(roleName: String): Flow<RolesWithEmployee>

    @Delete
    suspend fun deleteUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)
}
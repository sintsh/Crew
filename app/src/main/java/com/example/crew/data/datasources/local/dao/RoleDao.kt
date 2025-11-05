package com.example.crew.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.crew.data.datasources.local.entity.Role
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Insert
    suspend fun insertRole(role: Role): Long

    @Query("SELECT * FROM roles ORDER BY roleName ASC")
    fun getAllRoles(): Flow<List<Role>>

    @Transaction
    @Query("SELECT * FROM roles WHERE roleId = :roleId")
    fun getEmployeesByRole(roleId: Long): Flow<List<Role>>

    @Delete
    suspend fun deleteRole(role: Role)

    @Query("SELECT * FROM roles WHERE roleId = :roleId")
    suspend fun getRoleById(roleId: Long): Role
}
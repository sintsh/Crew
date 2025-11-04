package com.example.crew.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.data.datasources.local.entity.EmployeeWithRoles
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeRoleCrossRefDao {
    @Insert
    suspend fun insertUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)

    @Transaction
    @Query("SELECT * FROM employees")
    fun getEmployeesWithRoles(): Flow<List<EmployeeWithRoles>>

    @Delete
    suspend fun deleteUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)
}
package com.example.crew.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef

@Dao
interface EmployeeRoleCrossRefDao {
    @Insert
    suspend fun insertUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)

    @Delete
    suspend fun deleteUserRoleCrossRef(crossRef: EmployeeRoleCrossRef)
}
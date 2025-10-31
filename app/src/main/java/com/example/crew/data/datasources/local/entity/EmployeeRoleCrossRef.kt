package com.example.crew.data.datasources.local.entity

import androidx.room.Entity

@Entity(tableName = "employee_role_cross_ref", primaryKeys = ["employeeId", "roleId"])
data class EmployeeRoleCrossRef(
    val employeeId: Long,
    val roleId: Long
)

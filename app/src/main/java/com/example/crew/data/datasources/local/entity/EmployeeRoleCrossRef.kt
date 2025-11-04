package com.example.crew.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "employee_role_cross_ref",
    primaryKeys = ["employeeId", "roleId"],
    foreignKeys = [
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["employeeId"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Role::class,
            parentColumns = ["roleId"],
            childColumns = ["roleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class EmployeeRoleCrossRef(
    val employeeId: Long,
    val roleId: Long
)

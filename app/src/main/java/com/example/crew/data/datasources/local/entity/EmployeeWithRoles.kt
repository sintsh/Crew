package com.example.crew.data.datasources.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EmployeeWithRoles(
    @Embedded val employee: Employee,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "roleId",
        associateBy = Junction(EmployeeRoleCrossRef::class)
    )
    val roles: List<Role>
)

package com.example.crew.data.datasources.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RolesWithUsers(
    @Embedded val role: Role,
    @Relation(
        parentColumn = "roleId",
        entityColumn = "employeeId",
        associateBy = Junction(EmployeeRoleCrossRef::class)
    )
    val employees: List<Employee>
)

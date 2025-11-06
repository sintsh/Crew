package com.example.crew.data.datasources.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.crew.domain.entities.RolesWithEmployeeDE

class RolesWithEmployee (
        @Embedded val role: Role,
        @Relation(
            parentColumn = "roleId",
            entityColumn = "employeeId",
            associateBy = Junction(EmployeeRoleCrossRef::class)
        )
        val employees: List<Employee>
    )

    fun RolesWithEmployee.toEmployeeWithRolesDE(): RolesWithEmployeeDE {
        val employeeDE = employees.map {
            it.toEmployeeDE()
        }

        return RolesWithEmployeeDE(
            role = role.toRoleDE(),
            employees = employeeDE
        )
    }
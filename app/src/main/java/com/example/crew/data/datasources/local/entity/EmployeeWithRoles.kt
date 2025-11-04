package com.example.crew.data.datasources.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.crew.domain.entities.EmployeeWithRolesDE

data class EmployeeWithRoles(
    @Embedded val employee: Employee,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "roleId",
        associateBy = Junction(EmployeeRoleCrossRef::class)
    )
    val roles: List<Role>
)


fun EmployeeWithRoles.toEmployeeWithRolesDE(): EmployeeWithRolesDE {
    val rolesDE = roles.map {
        it.toRoleDE()
    }

    return EmployeeWithRolesDE(
        employee = employee.toEmployeeDE(),
        roles = rolesDE
        )
}

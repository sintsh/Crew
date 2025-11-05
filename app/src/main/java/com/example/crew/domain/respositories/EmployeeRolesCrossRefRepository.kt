package com.example.crew.domain.respositories

import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.domain.entities.EmployeeWithRolesDE
import kotlinx.coroutines.flow.Flow

interface EmployeeRolesCrossRefRepository {
    suspend fun getEmployeesWithRoles(): Flow<List<EmployeeWithRolesDE>>

    suspend fun addRoleForEmployee(employeeRoleCrossRef: EmployeeRoleCrossRef)

    suspend fun deleteRoleFromEmployee(employeeRoleCrossRef: EmployeeRoleCrossRef)
}
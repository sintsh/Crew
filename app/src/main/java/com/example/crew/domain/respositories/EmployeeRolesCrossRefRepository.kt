package com.example.crew.domain.respositories

import com.example.crew.domain.entities.EmployeeWithRolesDE
import kotlinx.coroutines.flow.Flow

interface EmployeeRolesCrossRefRepository {
    suspend fun getEmployeesWithRoles(): Flow<List<EmployeeWithRolesDE>>
}
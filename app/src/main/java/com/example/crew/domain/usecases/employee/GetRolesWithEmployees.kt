package com.example.crew.domain.usecases.employee

import com.example.crew.domain.entities.RolesWithEmployeeDE
import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRolesWithEmployees @Inject constructor(
    private val employeeRolesCrossRefRepository: EmployeeRolesCrossRefRepository
) {
    suspend operator fun invoke(): Flow<List<RolesWithEmployeeDE>>{
        return employeeRolesCrossRefRepository.getRolesWithEmployees()
    }
}
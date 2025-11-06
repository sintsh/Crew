package com.example.crew.domain.usecases.employee

import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEmployeesByRoleNameUseCase @Inject constructor(
    private val employeeRolesCrossRefRepository: EmployeeRolesCrossRefRepository
) {
    suspend operator fun invoke(roleName:String) = employeeRolesCrossRefRepository.getEmployeesByRoleName(roleName).map {
        it.employees
    }
}
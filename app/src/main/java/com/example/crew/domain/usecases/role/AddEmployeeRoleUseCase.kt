package com.example.crew.domain.usecases.role

import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import javax.inject.Inject

class AddEmployeeRoleUseCase @Inject constructor(
    private val employeeRolesCrossRefRepository: EmployeeRolesCrossRefRepository
) {
    suspend operator fun invoke(employeeRoleCrossRef: EmployeeRoleCrossRef){
        employeeRolesCrossRefRepository.addRoleForEmployee(employeeRoleCrossRef)
    }
}
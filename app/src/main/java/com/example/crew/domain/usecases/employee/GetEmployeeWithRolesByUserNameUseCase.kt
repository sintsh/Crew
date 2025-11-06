package com.example.crew.domain.usecases.employee

import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.domain.entities.EmployeeWithRolesDE
import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import javax.inject.Inject

class GetEmployeeWithRolesByUserNameUseCase @Inject constructor(
    private val employeeRolesCrossRefRepository: EmployeeRolesCrossRefRepository
) {
    suspend operator fun invoke(username:String): Result<EmployeeWithRolesDE>{
        return employeeRolesCrossRefRepository.getEmployeeWithRolesByUserName(username)
    }
}
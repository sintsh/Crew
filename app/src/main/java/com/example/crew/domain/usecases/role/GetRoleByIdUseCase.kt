package com.example.crew.domain.usecases.role

import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.respositories.RoleRepository
import javax.inject.Inject

class GetRoleByIdUseCase @Inject constructor(
    private val roleRepository: RoleRepository
) {
    suspend operator fun invoke(roleId: Long): RolesDE{
        return roleRepository.getRoleById(roleId)
    }
}
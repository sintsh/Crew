package com.example.crew.domain.usecases.role

import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.respositories.RoleRepository
import javax.inject.Inject

class InsertRoleUseCase @Inject constructor(
    private val roleRepository: RoleRepository
) {
    suspend operator fun invoke(roleDe: RolesDE): Long{
        return roleRepository.insertRole(roleDe)
    }
}
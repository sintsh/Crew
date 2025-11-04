package com.example.crew.domain.usecases.role

import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.respositories.RoleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRolesUseCase @Inject constructor(
    private val roleRepository: RoleRepository
) {

    suspend operator fun invoke(): Flow<List<RolesDE>>{
        return roleRepository.getAllRoles()
    }
}
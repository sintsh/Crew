package com.example.crew.domain.respositories

import com.example.crew.domain.entities.RolesDE
import kotlinx.coroutines.flow.Flow

interface RoleRepository {
    suspend fun getAllRoles(): Flow<List<RolesDE>>

    suspend fun insertRole(rolesDE: RolesDE): Long
}
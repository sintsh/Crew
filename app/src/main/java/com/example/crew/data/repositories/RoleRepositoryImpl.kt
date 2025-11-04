package com.example.crew.data.repositories

import com.example.crew.data.datasources.local.dao.RoleDao
import com.example.crew.data.datasources.local.entity.Role
import com.example.crew.data.datasources.local.entity.toRoleDE
import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.respositories.RoleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoleRepositoryImpl @Inject constructor(
    private val roleDao: RoleDao
): RoleRepository {
    override suspend fun getAllRoles(): Flow<List<RolesDE>> {
        return roleDao.getAllRoles().map { res ->
            res.map { role ->
                role.toRoleDE()
            }
        }
    }

    override suspend fun insertRole(rolesDE: RolesDE): Long {
        return roleDao.insertRole(Role(roleName = rolesDE.roleName))
    }
}
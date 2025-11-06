package com.example.crew.data.repositories

import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.data.datasources.local.dao.EmployeeRoleCrossRefDao
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.data.datasources.local.entity.EmployeeWithRoles
import com.example.crew.data.datasources.local.entity.toEmployeeWithRolesDE
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.entities.EmployeeWithRolesDE
import com.example.crew.domain.entities.RolesWithEmployeeDE
import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmployeeWithRolesRepositoryImpl @Inject constructor(
    private val employeeRoleCrossRefDao: EmployeeRoleCrossRefDao
): EmployeeRolesCrossRefRepository {
    override suspend fun getEmployeesWithRoles(): Flow<List<EmployeeWithRolesDE>> {
        return employeeRoleCrossRefDao.getEmployeesWithRoles().map { res->
            res.map {
                it.toEmployeeWithRolesDE()
            }
        }
    }

    override suspend fun getEmployeeWithRolesByUserName(username: String): Result<EmployeeWithRolesDE> {
        val employee = employeeRoleCrossRefDao.getEmployeesWithRolesByUserName(username)
        return if (employee!=null) Result.Success(employee.toEmployeeWithRolesDE())
        else Result.Error("Employee not found!")
    }

    override suspend fun getRolesWithEmployees(): Flow<List<RolesWithEmployeeDE>> {
        return employeeRoleCrossRefDao.getRoleWithEmployees().map { res->
            res.map {
                it.toEmployeeWithRolesDE()
            }
        }
    }

    override suspend fun addRoleForEmployee(employeeRoleCrossRef: EmployeeRoleCrossRef) {
        employeeRoleCrossRefDao.insertUserRoleCrossRef(employeeRoleCrossRef)
    }

    override suspend fun deleteRoleFromEmployee(employeeRoleCrossRef: EmployeeRoleCrossRef) {
        employeeRoleCrossRefDao.deleteUserRoleCrossRef(employeeRoleCrossRef)
    }

    override suspend fun getEmployeesByRoleName(roleName: String): Flow<RolesWithEmployeeDE> {
        return employeeRoleCrossRefDao.getEmployeesByRoleName(roleName)
            .map {
                it.toEmployeeWithRolesDE()
            }
    }
}
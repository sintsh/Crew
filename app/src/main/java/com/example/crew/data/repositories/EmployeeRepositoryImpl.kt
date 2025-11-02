package com.example.crew.data.repositories

import android.util.Log
import com.example.crew.data.datasources.local.dao.EmployeeDao
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.data.datasources.local.entity.toEmployeeDE
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao
): EmployeeRepository {
    override suspend fun getAllEmployees(): Flow<List<EmployeeDE>> {
        val employeeDe = mutableListOf<EmployeeDE>()
        Log.i("Crewhere", ": going to dao")

        return employeeDao.getAllEmployees().map { employees ->
            Log.i("Crewhere", ": gotten from dao $employees")

            employees.map { employee ->
                employee.toEmployeeDE()
            }
        }
    }

    override suspend fun saveEmployee(employee: Employee) {
        employeeDao.insertEmployee(employee)
    }

    override suspend fun deleteEmployee(employeeId: Long) {
        employeeDao.deleteEmployee(employeeId)
    }

    override suspend fun deleteAllEmployees() {
        employeeDao.deleteAllEmployees()
    }

    override suspend fun getEmployeeById(employeeId: Long): Employee {
        return employeeDao.getEmployeeById(employeeId)
    }

}
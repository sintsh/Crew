package com.example.crew.domain.usecases.employee

import com.example.crew.data.datasources.local.entity.toEmployeeDE
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(limit:Int=6, offset:Int = 0): Flow<List<EmployeeDE>> {
        return employeeRepository.getAllEmployees(limit, offset)
    }


    suspend fun getById(employeeId: Long): Flow<EmployeeDE?> {
        return employeeRepository.getEmployeeById(employeeId).map {
            it?.toEmployeeDE()
        }
    }

    suspend fun getEmployeeCount():Int{
        return employeeRepository.getEmployeeCount()
    }
}
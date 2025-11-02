package com.example.crew.domain.usecases

import android.util.Log
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.data.datasources.local.entity.toEmployeeDE
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(): Flow<List<EmployeeDE>> {
        Log.i("Crewhere", ": in usecase atleast")

        return employeeRepository.getAllEmployees()
    }


    suspend fun getById(employeeId: Long): Flow<EmployeeDE>{
        return flowOf(employeeRepository.getEmployeeById(employeeId).toEmployeeDE())
    }
}
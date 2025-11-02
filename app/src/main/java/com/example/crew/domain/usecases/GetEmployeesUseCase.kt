package com.example.crew.domain.usecases

import android.util.Log
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(): Flow<List<EmployeeDE>> {
        Log.i("Crewhere", ": in usecase atleast")

        return employeeRepository.getAllEmployees()
    }
}
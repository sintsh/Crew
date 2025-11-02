package com.example.crew.domain.usecases

import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class DeleteAllEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke() = employeeRepository.deleteAllEmployees()
}
package com.example.crew.domain.usecases.employee

import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {

    suspend operator fun invoke(employeeId: Long) = employeeRepository.deleteEmployee(employeeId)
}
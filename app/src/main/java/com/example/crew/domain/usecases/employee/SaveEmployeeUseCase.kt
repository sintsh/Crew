package com.example.crew.domain.usecases.employee

import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class SaveEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee) = employeeRepository.saveEmployee(employee)
}
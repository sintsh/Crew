package com.example.crew.domain.usecases.employee

import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class SaveEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee) {
        if (checkEmployeeSavable(employee)) {
            employeeRepository.saveEmployee(employee)
        }
    }

    fun checkEmployeeSavable(employee: Employee): Boolean{
        return (employee.employeeId>0)
                &&(employee.name.length>3)
                &&(employee.lastName.length>3)
                &&(employee.username.length>3)
                &&(employee.age>18)
                &&(employee.age<70)

    }
}


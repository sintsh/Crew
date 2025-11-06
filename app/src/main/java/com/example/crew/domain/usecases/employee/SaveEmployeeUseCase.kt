package com.example.crew.domain.usecases.employee

import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.data.datasources.local.entity.toEmployeeDE
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class SaveEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): Result<EmployeeDE> {
        if (checkEmployeeSavable(employee)) {
            try {
                employeeRepository.saveEmployee(employee)
                return Result.Success(employee.toEmployeeDE())
            }catch (e: Exception){
                return Result.Error(e.message.toString())
            }
        }else{
            return Result.Error("Employee is not savable")
        }
    }

    fun checkEmployeeSavable(employee: Employee): Boolean{
        return (employee.name.length>3)
                &&(employee.lastName.length>3)
                &&(employee.username.length>3)
                &&(employee.age>18)
                &&(employee.age<70)

    }
}


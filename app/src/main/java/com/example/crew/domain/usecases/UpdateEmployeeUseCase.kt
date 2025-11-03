package com.example.crew.domain.usecases

import com.example.crew.domain.respositories.EmployeeRepository
import javax.inject.Inject

class UpdateEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employeeId:Long, username:String, name:String, lastName:String, age:Int){
        employeeRepository.updateEmployee(employeeId,username, name, lastName, age)
    }
}
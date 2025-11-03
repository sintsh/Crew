package com.example.crew.domain.usecases

import com.example.crew.data.datasources.local.entity.PagedEmployee
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.respositories.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(query:String): Flow<List<EmployeeDE>>{
        return employeeRepository.searchEmployeeByQuery(query)
    }
}
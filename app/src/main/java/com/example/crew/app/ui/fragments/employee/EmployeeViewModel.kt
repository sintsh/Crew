package com.example.crew.app.ui.fragments.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crew.app.ui.helpers.login.LoggedInUserView
import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.app.ui.helpers.states.UIState
import com.example.crew.domain.entities.EmployeeWithRolesDE
import com.example.crew.domain.usecases.employee.GetEmployeeWithRolesByUserNameUseCase
import com.example.crew.domain.usecases.employee.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeeWithRolesByUserNameUseCase
) : ViewModel() {
    private val _employeeDetails = MutableStateFlow<Result<EmployeeWithRolesDE>>(Result.Loading())
    val employeeDetails = _employeeDetails.asStateFlow()



    fun fetchEmployeeDetails(username: String){
        _employeeDetails.value = Result.Loading()

        viewModelScope.launch {
            val result = getEmployeesUseCase(username)
            _employeeDetails.value = result
        }
    }
}
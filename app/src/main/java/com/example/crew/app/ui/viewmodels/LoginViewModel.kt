package com.example.crew.app.ui.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crew.R
import com.example.crew.app.ui.helpers.login.LoggedInUserView
import com.example.crew.app.ui.helpers.login.LoginFormState
import com.example.crew.app.ui.helpers.login.LoginResult
import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.app.ui.helpers.states.RoleType
import com.example.crew.data.datasources.local.entity.EmployeeWithRoles
import com.example.crew.domain.usecases.employee.GetEmployeeWithRolesByUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getEmployeeWithRolesByUserNameUseCase: GetEmployeeWithRolesByUserNameUseCase
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val employeeResult = getEmployeeWithRolesByUserNameUseCase(username)

            when(employeeResult){
                is Result.Error -> {
                    _loginResult.value = LoginResult(error = employeeResult.message)
                }
                is Result.Success -> {
                    val adminRole = employeeResult.data.roles.any {
                        it.roleName == "admin"
                    }

                    _loginResult.value = LoginResult(
                        success = LoggedInUserView(
                            displayName = employeeResult.data.employee.name,
                            adminRole = if (adminRole) RoleType.ADMIN else RoleType.NORMAL
                        ),

                    )


                }
            }
        }
//        val result = loginRepository.login(username, password)
//
//        if (result is Result.Success) {
//            _loginResult.value =
//                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
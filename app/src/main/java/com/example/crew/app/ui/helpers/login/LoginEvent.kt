package com.example.crew.app.ui.helpers.login

import com.example.crew.app.ui.helpers.states.PageType

sealed class LoginEvent {
    data class showToast(val message:String): LoginEvent()
    data class navigate(val route: PageType = PageType.EMPLOYEE, val username: String): LoginEvent()
}
package com.example.crew.app.ui.helpers.login

import com.example.crew.app.ui.helpers.states.RoleType

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val adminRole: RoleType = RoleType.NORMAL,
    val username: String?=null
)
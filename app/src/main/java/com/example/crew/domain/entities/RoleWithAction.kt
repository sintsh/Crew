package com.example.crew.domain.entities

import android.os.Parcelable
import com.example.crew.app.ui.helpers.admin.ActionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoleWithAction(
    val rolesDE: RolesDE,
    val action: ActionType
): Parcelable

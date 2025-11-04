package com.example.crew.domain.entities

import android.os.Parcelable
import com.example.crew.app.ui.helpers.admin.ActionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeWithAction(
    val employee: EmployeeDE,
    val action: ActionType
): Parcelable

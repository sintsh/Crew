package com.example.crew.app.ui.helpers.admin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ActionType: Parcelable {
    EDIT, CREATE, NULL
}
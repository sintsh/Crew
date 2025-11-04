package com.example.crew.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RolesDE(
    val roleId: Long = 0,
    val roleName: String
): Parcelable

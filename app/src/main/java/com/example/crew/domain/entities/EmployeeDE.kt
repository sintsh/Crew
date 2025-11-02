package com.example.crew.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeDE(
    val employeeId: Long = 0,
    val username: String,
    val name:String,
    val lastName:String,
    val age:Int,
): Parcelable

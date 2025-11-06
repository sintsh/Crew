package com.example.crew.domain.entities

data class RolesWithEmployeeDE(
    val role: RolesDE,
    val employees: List<EmployeeDE>
)
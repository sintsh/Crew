package com.example.crew.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crew.domain.entities.RolesDE

@Entity(tableName = "roles")
data class Role (
    @PrimaryKey(autoGenerate = true) val roleId: Long = 0,
    val roleName: String
)

fun Role.toRoleDE() = RolesDE(roleId, roleName)

fun RolesDE.toRole() = Role(this.roleId, this.roleName)

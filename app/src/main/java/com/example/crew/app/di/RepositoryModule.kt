package com.example.crew.app.di

import com.example.crew.data.repositories.EmployeeRepositoryImpl
import com.example.crew.data.repositories.EmployeeWithRolesRepositoryImpl
import com.example.crew.data.repositories.RoleRepositoryImpl
import com.example.crew.domain.respositories.EmployeeRepository
import com.example.crew.domain.respositories.EmployeeRolesCrossRefRepository
import com.example.crew.domain.respositories.RoleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindEmployeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository

    @Binds
    abstract fun bindRoleRepository(impl: RoleRepositoryImpl): RoleRepository

    @Binds
    abstract fun bindEmployeeWithRolesRepository(impl: EmployeeWithRolesRepositoryImpl): EmployeeRolesCrossRefRepository
}
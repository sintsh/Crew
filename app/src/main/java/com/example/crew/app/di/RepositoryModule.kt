package com.example.crew.app.di

import com.example.crew.data.repositories.EmployeeRepositoryImpl
import com.example.crew.domain.respositories.EmployeeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindEmployeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository
}
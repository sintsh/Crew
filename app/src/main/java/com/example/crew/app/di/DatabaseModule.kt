package com.example.crew.app.di

import android.app.Application
import androidx.room.Room
import com.example.crew.data.datasources.local.dao.EmployeeDao
import com.example.crew.data.datasources.local.dao.EmployeeRoleCrossRefDao
import com.example.crew.data.datasources.local.dao.RoleDao
import com.example.crew.data.datasources.local.database.CrewDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): CrewDatabase =
        Room
        .databaseBuilder(
            app,
            CrewDatabase::class.java,
            "crew_db.db",
            ).build()

    @Provides
    fun provideEmployeeDao(db: CrewDatabase): EmployeeDao = db.employeeDao()

    @Provides
    fun provideRoleDao(db: CrewDatabase): RoleDao = db.roleDao()

    @Provides
    fun provideEmployeeRoleCrossRefDao(db: CrewDatabase): EmployeeRoleCrossRefDao = db.employeeRoleCrossRefDao()
}
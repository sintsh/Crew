package com.example.crew.data.datasources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crew.data.datasources.local.dao.EmployeeDao
import com.example.crew.data.datasources.local.dao.EmployeeRoleCrossRefDao
import com.example.crew.data.datasources.local.dao.RoleDao
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.data.datasources.local.entity.EmployeeRoleCrossRef
import com.example.crew.data.datasources.local.entity.Role

@Database(entities = [Employee::class, Role::class, EmployeeRoleCrossRef::class], version = 1, exportSchema = false)
abstract class CrewDatabase: RoomDatabase(){
    abstract fun employeeDao(): EmployeeDao
    abstract fun roleDao(): RoleDao
    abstract fun employeeRoleCrossRefDao(): EmployeeRoleCrossRefDao


    companion object{
        @Volatile private var INSTANCE: CrewDatabase?=null

        fun getCrewDatabase(context: Context): CrewDatabase{
            return INSTANCE?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = CrewDatabase::class.java,
                    name = "").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
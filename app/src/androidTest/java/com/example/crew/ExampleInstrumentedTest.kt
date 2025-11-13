package com.example.crew

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.crew.app.ui.fragments.login.LoginFragment
import com.example.crew.data.datasources.local.dao.EmployeeDao
import com.example.crew.data.datasources.local.database.CrewDatabase
import com.example.crew.data.datasources.local.entity.Employee
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.manipulation.Ordering

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExampleInstrumentedTest {
    private lateinit var database: CrewDatabase
    private  lateinit var dao: EmployeeDao

    @Before
    fun setup(){
        database = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                   CrewDatabase::class.java
                )
            .allowMainThreadQueries()
            .build()

        dao = database.employeeDao()
    }


    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.crew", appContext.packageName)
    }


    @Test
    fun insertEmployee() = runBlocking {
        val testEmployee = Employee(0,"username","name","last name",24)
        val testId = dao.insertEmployee(testEmployee)

        assertTrue(testId != testEmployee.employeeId)

        val returnedEmployeeFlow = dao.getEmployeeById(testId)

        val returnedEmployee = returnedEmployeeFlow.first()

        assertEquals(testEmployee.name, returnedEmployee?.name)


    }


    @Test
    fun deleteEmployee() = runBlocking {
        val testEmployee = Employee(0,"username","name","last name",24)
        val testId = dao.insertEmployee(testEmployee)

        dao.deleteEmployee(testId)

        val returnedEmployeeFlowAfterDelete = dao.getEmployeeById(testId)

        val returnedEmployeeAfterDelete = returnedEmployeeFlowAfterDelete.first()

        assertTrue(returnedEmployeeAfterDelete == null)
    }

}
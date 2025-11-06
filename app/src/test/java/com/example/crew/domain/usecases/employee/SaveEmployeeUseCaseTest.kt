package com.example.crew.domain.usecases.employee

import com.example.crew.app.ui.viewmodels.AdminHomeViewModel
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.domain.respositories.EmployeeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SaveEmployeeUseCaseTest {
    @MockK
    private lateinit var myRepository: EmployeeRepository

    @MockK
    private lateinit var adminHomeViewModelMock: AdminHomeViewModel



    private lateinit var saveEmployeeUseCase: SaveEmployeeUseCase

    companion object{
        private const val TAG = "SAVE_EMPLOYEE_TAG"
        @BeforeAll
        @JvmStatic
        fun beforeAll(){
            print("beforeAll: Initializing")
        }

        @AfterAll
        @JvmStatic
        fun afterAll(){
            print("afterAll: Cleaning up")
        }

    }

    @BeforeEach
    fun beforeEach(){
        coEvery { myRepository.getEmployeeById(1) } returns Employee(1, "johny","John","Jacobson",19)


        saveEmployeeUseCase = SaveEmployeeUseCase(myRepository)

    }

    @Test
    fun `test employee values with correct data inserted`(){
        //arrange
        val employee = Employee(1, "johny","John","Jacobson",19)

        adminHomeViewModelMock.addEmployee(employee)

        coVerify {
            myRepository.saveEmployee(employee)
        }

        val result = saveEmployeeUseCase.checkEmployeeSavable(employee)

        //assert
        Assertions.assertTrue(result)
    }


    @Test
    fun `test employee values with incorrect age inserted`(){
        //arrange
        val employee = Employee(1, "johny","John","Jacobson",18)

        val result = saveEmployeeUseCase.checkEmployeeSavable(employee)

        //assert
        Assertions.assertFalse(result)
    }

}
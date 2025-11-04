package com.example.crew.app.ui.dialogs

import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.crew.R
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.databinding.FragmentEmployeeActionDialogBinding
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.entities.EmployeeWithAction
import com.google.android.material.textfield.TextInputEditText


class EmployeeActionDialogFragment : DialogFragment() {

private val employeeDialogArgs: EmployeeActionDialogFragmentArgs by navArgs()

    private lateinit var usernameField : TextInputEditText
    private lateinit var nameField : TextInputEditText
    private lateinit var lastNameField : TextInputEditText
    private lateinit var ageField : TextInputEditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEmployeeActionDialogBinding.inflate(inflater, container, false)

        usernameField = binding.userName
        nameField = binding.name
        lastNameField = binding.lastName
        ageField = binding.age

        when(employeeDialogArgs.actionType){
            ActionType.EDIT -> {
                employeeDialogArgs.employee?.let { emp->
                    usernameField.setText(emp.username)

                    nameField.setText(emp.name)

                    lastNameField.setText(emp.lastName)

                    ageField.setText(emp.age.toString())
                }
            }
            ActionType.CREATE -> {

            }

            ActionType.NULL -> {}
        }

        binding.saveButton.setOnClickListener {
            Log.i("Dialog employee", "onCreateView: save button")
            val username = binding.userName.text.toString()
            val name = binding.name.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString().toIntOrNull()

            if ((username.isNotEmpty())&&(name.isNotEmpty())&&(lastName.isNotEmpty())&&(age!=null)){
                val employeeFromDialog = EmployeeDE(employeeId = employeeDialogArgs.employee?.employeeId, username =  username, name = name, lastName = lastName, age = age)
                val action = employeeDialogArgs.actionType
                val employeeWithAction = EmployeeWithAction(employeeFromDialog, action)
                Log.i("checkthisout", "to be sent: data found \n\t $employeeWithAction")

                val nav = findNavController()
                nav.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("employee_with_action_result",employeeWithAction)
                nav.navigateUp()
            }
        }


        binding.cancelButton.setOnClickListener { findNavController().popBackStack() }

    return binding.root
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (Resources.getSystem().displayMetrics.widthPixels * 0.80).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
    }

}
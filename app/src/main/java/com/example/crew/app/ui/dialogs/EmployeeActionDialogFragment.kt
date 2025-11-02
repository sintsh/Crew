package com.example.crew.app.ui.dialogs

import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.crew.R
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.databinding.FragmentEmployeeActionDialogBinding
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
                usernameField.setText(employeeDialogArgs.employee.username)

                nameField.setText(employeeDialogArgs.employee.name)

                lastNameField.setText(employeeDialogArgs.employee.lastName)

                ageField.setText(employeeDialogArgs.employee.age.toString())
            }
            ActionType.CREATE -> TODO()
        }

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
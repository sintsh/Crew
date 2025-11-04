package com.example.crew.app.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.crew.R
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.databinding.FragmentRoleDialogBinding
import com.example.crew.domain.entities.RoleWithAction
import com.example.crew.domain.entities.RolesDE

class RoleDialogFragment : DialogFragment(R.layout.fragment_role_dialog) {

    val args : RoleDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRoleDialogBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        if (args.action == ActionType.EDIT){
            args.role?.let { role->
                binding.roleName.setText(role.roleName)
            }
        }

        binding.saveButton.setOnClickListener {
            val roleName = binding.roleName.text.toString()

            val roleWithAction = RoleWithAction(RolesDE(roleName = roleName), args.action)

            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("role_with_action", roleWithAction)

            findNavController().popBackStack()

        }





    return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoleDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
package com.example.crew.app.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.app.ui.adapters.RoleListRecyclerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.viewmodels.AdminRolesViewModel
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.databinding.FragmentAdminRolesBinding
import kotlin.getValue

class AdminRolesFragment : Fragment(R.layout.fragment_admin_home) {

    private val viewModel: AdminRolesViewModel by viewModels()

    private var _binding : FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var roleAdapter : RoleListRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
    }



    private fun setupRecyclerView() {
        roleAdapter = RoleListRecyclerAdapter{ click ->
            when (click) {
                is RoleListRecyclerAdapter.RoleClickable.DeleteClick -> {
                   // viewModel.deleteRole(click.roleId)
                }
                is RoleListRecyclerAdapter.RoleClickable.EditClick -> {
                   // navigateToEditPage(click.roleId)
                }
            }
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = roleAdapter
        }
    }


    private fun setupClickListeners() {
        binding.back.setOnClickListener {
           // viewModel.onPreviousPage()
        }

        binding.next.setOnClickListener {
          //  viewModel.onNextPage()
        }

        binding.createButton.setOnClickListener {
            val nav = findNavController()
            val  direction = AdminRolesFragmentDirections.actionAdminRolesFragmentToRoleDialogFragment()//null, actionType = ActionType.CREATE)
            nav.navigate(direction)
        }

       // binding.deleteAllButton.setOnClickListener { viewModel.deleteAllRoles() }
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}
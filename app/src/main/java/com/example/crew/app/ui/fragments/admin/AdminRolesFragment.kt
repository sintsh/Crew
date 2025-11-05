package com.example.crew.app.ui.fragments.admin

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.app.ui.adapters.RoleListRecyclerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.viewmodels.AdminRolesViewModel
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.databinding.FragmentAdminRolesBinding
import com.example.crew.domain.entities.RoleWithAction
import com.example.crew.domain.entities.RolesDE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
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

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<RoleWithAction>("role_with_action")
            ?.observe(viewLifecycleOwner){ roleWithAction ->
                when(roleWithAction.action){
                    ActionType.EDIT -> {

                    }
                    ActionType.CREATE -> {
                        viewModel.addRole(roleWithAction.rolesDE)
                    }
                    ActionType.NULL -> {}
                }
            }

        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }



    private fun setupRecyclerView() {
        roleAdapter = RoleListRecyclerAdapter{ click ->
            when (click) {
                is RoleListRecyclerAdapter.RoleClickable.DeleteClick -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete role <${click.role.roleName}>!")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("continue"){dialog,_->
                            viewModel.deleteRole(click.role.roleId)
                            Snackbar.make(binding.root,"undo", Snackbar.LENGTH_SHORT)
                                .setText("Undo the deleted role")
                                .setAction("Undo"){
                                        viewModel.addRole(click.role)
                                }
                                .show()
                        }
                        .show()
                }
                is RoleListRecyclerAdapter.RoleClickable.EditClick -> {
                    navigateToEditPage(click.role)
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
            val  direction = AdminMainFragmentDirections.actionAdminMainFragmentToRoleDialogFragment(
                action = ActionType.CREATE
            )//null, actionType = ActionType.CREATE)
            nav.navigate(direction)
        }

       // binding.deleteAllButton.setOnClickListener { viewModel.deleteAllRoles() }
    }



    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.roles.collectLatest { employees ->
                        roleAdapter.submitList(employees)
                    }
                }

                launch {
                    viewModel.offset.collectLatest { pageIndex ->
                        binding.page.text = "Page ${pageIndex + 1}"
                    }
                }

                launch {
                    viewModel.hasNextPage.collectLatest { hasNext ->
                        binding.next.visibility = if(hasNext) View.VISIBLE else View.INVISIBLE
                    }
                }

                launch {
                    viewModel.hasPreviousPage.collectLatest { hasPrevious ->
                        binding.back.visibility = if(hasPrevious) View.VISIBLE else View.INVISIBLE
                    }
                }
            }
        }
    }


    fun navigateToEditPage(role: RolesDE){
        val direction = AdminMainFragmentDirections.actionAdminMainFragmentToRoleDialogFragment(
            role = role, action = ActionType.EDIT
        )
        findNavController().navigate(direction)
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}
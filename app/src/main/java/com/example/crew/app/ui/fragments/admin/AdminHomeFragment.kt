package com.example.crew.app.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.viewmodels.AdminHomeViewModel
import com.example.crew.data.datasources.local.entity.Employee
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.domain.entities.EmployeeDE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private val viewModel: AdminHomeViewModel by viewModels()

    private lateinit var recycler: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAdminHomeBinding.inflate(inflater, container, false)

        recycler = binding.recycler


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.employeeList.collectLatest { employees ->
                    Log.i("BindingEMployees", "onBindViewHolder: \n${employees}")

                    val adapter = EmployeeListRecyclerAdapter(
                        employees
                    ){click->
                        when(click){
                            is EmployeeListRecyclerAdapter.EmployeeClickable.DeleteClick -> {
                                viewModel.deleteEmployee(click.employeeId)
                                Snackbar.make(requireContext(),binding.root, "Delete Functionality is not yet implemented",
                                    Snackbar.LENGTH_SHORT).show()
                            }
                            is EmployeeListRecyclerAdapter.EmployeeClickable.EditClick -> {
                                navigateToEditPage(click.employeeId)
                            }
                        }
                    }
                    recycler.layoutManager = LinearLayoutManager(requireContext())
                    recycler.adapter = adapter
                }
            }
        }


        binding.createButton.setOnClickListener {
            viewModel.addEmployee(Employee(username = "random",name = "random", lastName = "random", age = 26))
            Snackbar.make(requireContext(),binding.root, "Added employees",
                Snackbar.LENGTH_SHORT).show()
        }

        binding.deleteAllButton.setOnClickListener { viewModel.deleteAllEmployees() }

    return binding.root
    }


    private fun navigateToEditPage(employeeId:Long){
        viewLifecycleOwner.lifecycleScope.launch {
            val employee = viewModel.getEmployeeById(employeeId).collectLatest { employeeDE ->
                val nav = findNavController()
                val direction = AdminHomeFragmentDirections.actionAdminHomeFragmentToEmployeeActionDialogFragment(employeeDE,
                    ActionType.EDIT)
                nav.navigate(direction)
            }

        }
    }
}
package com.example.crew.ui.fragments.admin

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.ui.viewmodels.AdminHomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    companion object {
        fun newInstance() = AdminHomeFragment()
    }

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
                                Snackbar.make(requireContext(),binding.root, "Delete Functionality is not yet implemented",
                                    Snackbar.LENGTH_SHORT).show()
                            }
                            is EmployeeListRecyclerAdapter.EmployeeClickable.EditClick -> {
                                Snackbar.make(requireContext(),binding.root, "Edit Functionality is not yet implemented",
                                    Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                    recycler.layoutManager = LinearLayoutManager(requireContext())
                    recycler.adapter = adapter
                }
            }
        }


        binding.createButton.setOnClickListener {
            viewModel.addEmployee()
            Snackbar.make(requireContext(),binding.root, "Added employees",
                Snackbar.LENGTH_SHORT).show()
        }

    return binding.root
    }
}
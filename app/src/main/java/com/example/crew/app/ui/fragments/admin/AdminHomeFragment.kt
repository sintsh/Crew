package com.example.crew.app.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.viewmodels.AdminHomeViewModel
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.domain.entities.toEmployee
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private val viewModel: AdminHomeViewModel by viewModels()

    private lateinit var employeeAdapter: EmployeeListRecyclerAdapter

    private val adminArgs : AdminHomeFragmentArgs by navArgs()

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)

        adminArgs.employeeFromDialog?.let { employeeDE ->
            when(adminArgs.actionType){
                ActionType.EDIT -> {
                    Log.i("checkoutEmployeeData", "emp : $employeeDE")
                    employeeDE.employeeId?.let {
                        viewModel.updateEmployee(
                            employeeDE.employeeId,
                            employeeDE.username,
                            employeeDE.name,
                            employeeDE.lastName,
                            employeeDE.age
                        )
                    }
                }
                ActionType.CREATE -> {
                    viewModel.addEmployee(employeeDE.toEmployee())
                }
                ActionType.NULL -> {}
            }
        }

    return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }


    private fun setupRecyclerView() {
        employeeAdapter = EmployeeListRecyclerAdapter{ click ->
            when (click) {
                is EmployeeListRecyclerAdapter.EmployeeClickable.DeleteClick -> {
                    viewModel.deleteEmployee(click.employeeId)
                }
                is EmployeeListRecyclerAdapter.EmployeeClickable.EditClick -> {
                    navigateToEditPage(click.employeeId)
                }
            }
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = employeeAdapter
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.employeeList.collectLatest { employees ->
                        employeeAdapter.submitList(employees)
                    }
                }

                launch {
                    viewModel.offset.collectLatest { pageIndex ->
                        binding.page.text = "Page ${pageIndex + 1}"
                    }
                }

                launch {
                    viewModel.hasNextPage.collectLatest { hasNext ->
                        binding.next.isVisible = hasNext
                    }
                }

                launch {
                    viewModel.hasPreviousPage.collectLatest { hasPrevious ->
                        binding.back.isVisible = hasPrevious
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.back.setOnClickListener {
            viewModel.onPreviousPage()
        }

        binding.next.setOnClickListener {
            viewModel.onNextPage()
        }

        binding.createButton.setOnClickListener {
            val nav = findNavController()
            val  direction = AdminHomeFragmentDirections.actionAdminHomeFragmentToEmployeeActionDialogFragment(null, actionType = ActionType.CREATE)
            nav.navigate(direction)
        }

        binding.deleteAllButton.setOnClickListener { viewModel.deleteAllEmployees() }
    }


    private fun navigateToEditPage(employeeId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getEmployeeById(employeeId).collectLatest {
                Log.i("checkoutEmployeeData", "navigateToEditPage: \n $it")
                val direction = AdminHomeFragmentDirections.actionAdminHomeFragmentToEmployeeActionDialogFragment(
                    it,
                    ActionType.EDIT
                )
                findNavController().navigate(direction)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
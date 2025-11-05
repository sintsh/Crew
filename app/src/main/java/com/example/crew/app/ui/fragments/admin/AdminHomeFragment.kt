package com.example.crew.app.ui.fragments.admin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeListRecyclerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.viewmodels.AdminHomeViewModel
import com.example.crew.databinding.FragmentAdminHomeBinding
import com.example.crew.domain.entities.EmployeeWithAction
import com.example.crew.domain.entities.toEmployee
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminHomeFragment : Fragment(R.layout.fragment_admin_home) {

    private val viewModel: AdminHomeViewModel by viewModels()

    private lateinit var employeeAdapter: EmployeeListRecyclerAdapter


    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)


        binding.searchBtn.setOnClickListener {
            binding.searching.visibility = if(!(binding.searching.isVisible)){
                activity?.let {
                    WindowCompat.getInsetsController(it.window, binding.searching).show(WindowInsetsCompat.Type.ime())
                }
                binding.searching.requestFocus()

                View.VISIBLE
            } else{
                hideKeyboard(binding.searching)
                View.INVISIBLE
            }
        }

        binding.searching.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.setQuery(s.toString())
            }

        })

    return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<EmployeeWithAction>("employee_with_action_result")
            ?.observe(viewLifecycleOwner){ employeeWithAction ->
                if (employeeWithAction.action == ActionType.CREATE){
                    viewModel.addEmployee(employeeWithAction.employee.toEmployee())
                }else if(employeeWithAction.action == ActionType.EDIT){
                    employeeWithAction.employee.also { emp->
                        emp.employeeId?.let { id->
                            viewModel.updateEmployee(
                                id,
                                emp.username,
                                emp.name,
                                emp.lastName,
                                emp.age
                            )
                        }
                    }

                }
            }

        setupRecyclerView()
        setupClickListeners()
        observeViewModelState()
    }


    private fun setupRecyclerView() {
        employeeAdapter = EmployeeListRecyclerAdapter(rolesFLow = viewModel.roles,
            roleActions = {
                when(it){
                    is EmployeeListRecyclerAdapter.DataReceiver.RoleData -> {
                        viewModel.addEmployeeRole(it.employeeId,it.selected)
                        viewModel.deleteEmployeeRole(it.employeeId,it.unselected)
                    }
                }
            },
            onClick = { click ->
            when (click) {
                is EmployeeListRecyclerAdapter.EmployeeClickable.DeleteClick -> {
                    viewModel.deleteEmployee(click.employeeId)
                }
                is EmployeeListRecyclerAdapter.EmployeeClickable.EditClick -> {
                    navigateToEditPage(click.employeeId)
                }
            }
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = employeeAdapter
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.employeeWithRoles.collectLatest { employees ->
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

    private fun setupClickListeners() {
        binding.back.setOnClickListener {
            viewModel.onPreviousPage()
        }

        binding.next.setOnClickListener {
            viewModel.onNextPage()
        }

        binding.createButton.setOnClickListener {
            val nav = findNavController()
            val  direction = AdminMainFragmentDirections.actionAdminMainFragmentToEmployeeActionDialogFragment(null, actionType = ActionType.CREATE)
            nav.navigate(direction)
        }

        binding.deleteAllButton.setOnClickListener { viewModel.deleteAllEmployees() }
    }


    private fun navigateToEditPage(employeeId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getEmployeeById(employeeId).collectLatest {
                Log.i("checkoutEmployeeData", "navigateToEditPage: \n $it")
                val direction = AdminMainFragmentDirections.actionAdminMainFragmentToEmployeeActionDialogFragment(
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


    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }


    companion object {
        private const val ARG_USER_TYPE = "actionType"

        fun newInstance(userType: ActionType): AdminHomeFragment {
            val fragment = AdminHomeFragment()
            val args = Bundle().apply {
                putParcelable(ARG_USER_TYPE, userType)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
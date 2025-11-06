package com.example.crew.app.ui.fragments.employee

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.crew.R
import com.example.crew.app.ui.helpers.states.Result
import com.example.crew.databinding.FragmentEmployeeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmployeeFragment : Fragment(R.layout.fragment_employee) {

    companion object {
        fun newInstance() = EmployeeFragment()
    }

    private val viewModel: EmployeeViewModel by viewModels()
    private val args : EmployeeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEmployeeBinding.inflate(inflater, container, false)

        val username = args.username
        viewModel.fetchEmployeeDetails(username)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.employeeDetails.collectLatest { employeeResult ->
                    when(employeeResult){
                        is Result.Error -> {
                            binding.loadingBar.visibility = View.INVISIBLE
                        }
                        is Result.Loading<*> -> {
                            binding.loadingBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.loadingBar.visibility = View.INVISIBLE
                            binding.welcomeText.text = username
                        }
                    }
                }
            }
        }


        binding.logout.setOnClickListener {
            val nav = findNavController()
            nav.navigate(EmployeeFragmentDirections.actionEmployeeToLogout())
        }

    return binding.root
    }
}
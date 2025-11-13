package com.example.crew.app.ui.fragments.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.crew.R
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.app.ui.helpers.login.LoggedInUserView
import com.example.crew.app.ui.helpers.login.LoginEvent
import com.example.crew.app.ui.helpers.states.PageType
import com.example.crew.app.ui.helpers.states.RoleType
import com.example.crew.app.ui.viewmodels.LoginViewModel
import com.example.crew.app.ui.viewmodels.LoginViewModelFactory
import com.example.crew.databinding.LoginLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_layout) {

    private val loginViewModel: LoginViewModel by viewModels()

    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         _binding = LoginLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                login.isEnabled = false
                loading.visibility = View.VISIBLE


                updateUiWithUser(loginResult.success)


                loading.visibility = View.INVISIBLE
                login.isEnabled = true
            }
        })


        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.actions.collect { action->
                when(action){
                    is LoginEvent.navigate -> {
                        val nav = findNavController()

                        val direction = if (action.route == PageType.ADMIN) {
                            //navGraph.setStartDestination(R.id.adminMainFragment)
                            LoginFragmentDirections.actionLoginFragmentToAdminMainFragment()
                        }
                        else {
                            LoginFragmentDirections.actionLoginFragmentToEmployeeFragment(action.username)
                        }
                        nav.navigate(direction)
                      //  findNavController().graph = navGraph
                    }
                    is LoginEvent.showToast -> {
                        Toast.makeText(context?.applicationContext, action.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }



    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            context?.applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(context?.applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
package com.example.crew.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.crew.R
import com.example.crew.ui.viewmodels.AdminHomeViewModel

class AdminHomeFragment : Fragment() {

    companion object {
        fun newInstance() = AdminHomeFragment()
    }

    private val viewModel: AdminHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }
}
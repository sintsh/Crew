package com.example.crew.app.ui.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crew.R
import com.example.crew.app.ui.adapters.EmployeeViewPagerAdapter
import com.example.crew.app.ui.helpers.admin.ActionType
import com.example.crew.databinding.FragmentAdminMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class AdminMainFragment : Fragment(R.layout.fragment_admin_main) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminMainBinding.inflate(inflater, container, false)

        val bottomNavView = binding.bottomNavView
        val viewPager2 = binding.adminFragHolder

        val fragmentList = arrayListOf<Fragment>(
            AdminHomeFragment.newInstance(ActionType.NULL),
            AdminRolesFragment(),
        )


        val viewPagerAdapter = EmployeeViewPagerAdapter(this)
        for (fragment in fragmentList) viewPagerAdapter.addFragment(fragment)
        viewPager2.adapter = viewPagerAdapter
        val pageNameOptions = listOf<String>("Employee", "Roles")

        TabLayoutMediator(bottomNavView, viewPager2){ tab, pos->
            tab.text = pageNameOptions[pos%3]
        }.attach()

        return binding.root
    }
}
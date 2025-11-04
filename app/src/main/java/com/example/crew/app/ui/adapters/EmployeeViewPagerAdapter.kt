package com.example.crew.app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class EmployeeViewPagerAdapter (fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    val fragmentList = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount(): Int = fragmentList.size

}
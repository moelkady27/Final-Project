package com.example.finalproject.ui.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalproject.ui.profile.fragment.ListingsFragment
import com.example.finalproject.ui.profile.fragment.PendingFragment
import com.example.finalproject.ui.profile.fragment.SoldFragment

class MyViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PendingFragment()
            }
            1 -> {
                ListingsFragment()
            }
            2 -> {
                SoldFragment()
            }
            else -> {
                PendingFragment()
            }
        }
    }
}
package com.example.finalproject.ui.profile.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

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
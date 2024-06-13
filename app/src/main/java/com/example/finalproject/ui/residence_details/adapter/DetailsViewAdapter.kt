package com.example.finalproject.ui.residence_details.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalproject.ui.residence_details.fragment.DescriptionFragment
import com.example.finalproject.ui.residence_details.fragment.GalleryFragment
import com.example.finalproject.ui.residence_details.fragment.ReviewFragment

class DetailsViewAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DescriptionFragment()
            }
            1 -> {
                GalleryFragment()
            }
            2 -> {
                ReviewFragment()
            }
            else -> {
                DescriptionFragment()
            }
        }
    }
}
package com.example.finalproject.ui.profile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.profile.adapter.ListingsAdapter

class ListingsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listingsAdapter: ListingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycle_listings_profile)

        listingsAdapter = ListingsAdapter()

        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listingsAdapter
    }

}
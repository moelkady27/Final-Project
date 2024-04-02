package com.example.finalproject.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.home.adapter.HomeFeaturedAdapter
import com.example.finalproject.ui.home.adapter.HomePopularAdapter

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var homePopularAdapter: HomePopularAdapter
    private lateinit var homeFeaturedAdapter: HomeFeaturedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_home_popular)

        homePopularAdapter = HomePopularAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext() ,
            LinearLayoutManager.HORIZONTAL , false)
        recyclerView.adapter = homePopularAdapter

        recyclerView = view.findViewById(R.id.rv_home_featured)

        homeFeaturedAdapter = HomeFeaturedAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext() ,
            LinearLayoutManager.VERTICAL , false)
        recyclerView.adapter = homeFeaturedAdapter

    }
}
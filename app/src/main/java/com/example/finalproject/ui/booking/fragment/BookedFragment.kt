package com.example.finalproject.ui.booking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.booking.adapter.BookedAdapter

class BookedFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView

    private lateinit var bookedAdapter: BookedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_booked,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycle_booked_residences)
        recyclerView.layoutManager = LinearLayoutManager(requireContext() ,
            LinearLayoutManager.VERTICAL , false)
        bookedAdapter = BookedAdapter()
        recyclerView.adapter = bookedAdapter
    }
}
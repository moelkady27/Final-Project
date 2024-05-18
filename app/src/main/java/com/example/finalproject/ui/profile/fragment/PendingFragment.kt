package com.example.finalproject.ui.profile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.profile.adapter.PendingAdapter
import com.example.finalproject.ui.profile.factory.PendingFactory
import com.example.finalproject.ui.profile.repository.PendingRepository
import com.example.finalproject.ui.profile.viewModels.PendingViewModel

class PendingFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView
    private lateinit var pendingAdapter: PendingAdapter

    private lateinit var pendingViewModel: PendingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycle_pending_profile)

//        pendingAdapter = PendingAdapter()

        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)
        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = pendingAdapter

        initView()
    }

    private fun initView() {
        val pendingRepository = PendingRepository(RetrofitClient.instance)
        val factory = PendingFactory(pendingRepository)
        pendingViewModel = ViewModelProvider(
            this@PendingFragment,
            factory
        )[PendingViewModel::class.java]

        pendingViewModel.getPending(AppReferences.getToken(requireContext()))

        pendingViewModel.pendingResponseLiveData.observe(viewLifecycleOwner) {response ->
            response.let {

                val status = it.status
                pendingAdapter = PendingAdapter(it.residence)
                recyclerView.adapter = pendingAdapter

                Log.e("status", status)
            }
        }
    }

}
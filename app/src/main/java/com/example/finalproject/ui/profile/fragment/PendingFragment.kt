package com.example.finalproject.ui.profile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_pending.tv_pending_title_1
import org.json.JSONException
import org.json.JSONObject

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
        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)

        initView()
    }

    private fun initView() {
        val pendingRepository = PendingRepository(RetrofitClient.instance)
        val factory = PendingFactory(pendingRepository)
        pendingViewModel = ViewModelProvider(
            this@PendingFragment,
            factory
        )[PendingViewModel::class.java]

        pendingAdapter = PendingAdapter(mutableListOf())
        recyclerView.adapter = pendingAdapter

        val token = AppReferences.getToken(requireContext())
        pendingViewModel.getPending(token)

        pendingViewModel.pendingResponseLiveData.observe(viewLifecycleOwner) {response ->
            BaseActivity().hideProgressDialog()
            response?.let {

                val status = it.status
                Log.e("status", status)

                it.residences?.let { residence ->
                    pendingAdapter.addItems(residence)
                }

                tv_pending_title_1.text = pendingAdapter.itemCount.toString()            }
        }

        pendingViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
        initPagination(token)
    }

    private fun initPagination(token: String) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 >= totalItemCount) {
                    pendingViewModel.getPending(token)
                }
            }
        })
    }
}
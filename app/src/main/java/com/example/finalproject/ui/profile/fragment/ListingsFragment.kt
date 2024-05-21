package com.example.finalproject.ui.profile.fragment

import android.os.Bundle
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
import com.example.finalproject.ui.profile.adapter.ListingsAdapter
import com.example.finalproject.ui.profile.adapter.PendingAdapter
import com.example.finalproject.ui.profile.factory.ApprovedFactory
import com.example.finalproject.ui.profile.repository.ApprovedRepository
import com.example.finalproject.ui.profile.viewModels.ApprovedViewModel
import kotlinx.android.synthetic.main.fragment_listings.tv_listings_title_1
import org.json.JSONException
import org.json.JSONObject

class ListingsFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView
    private lateinit var listingsAdapter: ListingsAdapter

    private lateinit var approvedViewModel: ApprovedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
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
        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)

        initView()
    }

    private fun initView() {
        val approvedRepository = ApprovedRepository(RetrofitClient.instance)
        val factory = ApprovedFactory(approvedRepository)
        approvedViewModel = ViewModelProvider(this@ListingsFragment, factory
        )[ApprovedViewModel::class.java]

        listingsAdapter = ListingsAdapter(mutableListOf())
        recyclerView.adapter = listingsAdapter

        approvedViewModel.getApproved(AppReferences.getToken(requireContext()))

        approvedViewModel.approvedResponseLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {

                listingsAdapter.addItems(it.residences)
                tv_listings_title_1.text = listingsAdapter.itemCount.toString()
            }
        }

        approvedViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
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
        initPagination()
    }

    private fun initPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 >= totalItemCount) {
                    approvedViewModel.getApproved(AppReferences.getToken(requireContext()))
                }
            }
        })
    }
}
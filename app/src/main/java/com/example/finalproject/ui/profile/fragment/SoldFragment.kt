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
import com.example.finalproject.ui.profile.adapter.SoldAdapter
import com.example.finalproject.ui.profile.factory.SoldFactory
import com.example.finalproject.ui.profile.repository.SoldRepository
import com.example.finalproject.ui.profile.viewModels.SoldViewModel
import kotlinx.android.synthetic.main.fragment_sold.tv_sold_title_1
import org.json.JSONException
import org.json.JSONObject

class SoldFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView

    private lateinit var soldAdapter: SoldAdapter

    private lateinit var soldViewModel: SoldViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = requireView().findViewById(R.id.recycle_sold_profile)
        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)

        initView()
    }

    private fun initView() {
        val soldRepository = SoldRepository(RetrofitClient.instance)
        val factory = SoldFactory(soldRepository)
        soldViewModel = ViewModelProvider(this@SoldFragment, factory
        )[SoldViewModel::class.java]

        soldAdapter = SoldAdapter(mutableListOf())
        recyclerView.adapter = soldAdapter

        val token = AppReferences.getToken(requireContext())

        soldViewModel.getSold(token)

        soldViewModel.soldResponseLiveData.observe(viewLifecycleOwner) { response ->
            BaseActivity().hideProgressDialog()
            response?.let {
                val status = it.status
                Log.e("status", status)

                it.residences?.let { residence ->
                    soldAdapter.addItems(residence)
                }

                tv_sold_title_1.text = soldAdapter.itemCount.toString()
            }
        }

        soldViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
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
                    soldViewModel.getSold(token)
                }
            }
        })
    }

}
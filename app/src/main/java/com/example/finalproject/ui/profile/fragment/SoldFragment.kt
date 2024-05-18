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

        recyclerView = view.findViewById(R.id.recycle_sold_profile)
        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)
        recyclerView.setHasFixedSize(true)

        initView()
    }

    private fun initView() {
        val soldRepository = SoldRepository(RetrofitClient.instance)
        val factory = SoldFactory(soldRepository)
        soldViewModel = ViewModelProvider(this@SoldFragment, factory
        )[SoldViewModel::class.java]

        soldViewModel.getSold(AppReferences.getToken(requireContext()))

        soldViewModel.soldResponseLiveData.observe(viewLifecycleOwner) { response ->
            BaseActivity().hideProgressDialog()
            response?.let {
                soldAdapter = SoldAdapter(it.residence)
                recyclerView.adapter = soldAdapter

                tv_sold_title_1.text = it.count.toString()
            }
        }

        soldViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
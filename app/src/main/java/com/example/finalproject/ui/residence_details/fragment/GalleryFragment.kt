package com.example.finalproject.ui.residence_details.fragment

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
import com.example.finalproject.ui.residence_details.adapter.GalleryAdapter
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import org.json.JSONException
import org.json.JSONObject

class GalleryFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycle_gallery_details)
        recyclerView.layoutManager = GridLayoutManager(requireContext() , 2)

        galleryAdapter = GalleryAdapter(mutableListOf())
        recyclerView.adapter = galleryAdapter

        initView()
    }

    private fun initView() {

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@GalleryFragment, factoryGetResidence)[GetResidenceViewModel::class.java]

        val token = AppReferences.getToken(requireContext())
        val residenceId = arguments?.getString("residenceId")

        if (residenceId != null) {
            getResidenceViewModel.getResidence(token, residenceId)

            getResidenceViewModel.getResidenceResponseLiveData.observe(viewLifecycleOwner) { response ->
                baseActivity.hideProgressDialog()
                response?.let {
                    val images = it.residence.images.map { image -> image.url }
                    galleryAdapter.updateList(images)
                }
            }

            getResidenceViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
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
        } else {
            Toast.makeText(requireContext(), "Residence ID is not available", Toast.LENGTH_LONG).show()
        }
    }

}
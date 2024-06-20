package com.example.finalproject.ui.booking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.booking.adapter.BookedAdapter
import com.example.finalproject.ui.booking.factory.PurchaseResidenceFactory
import com.example.finalproject.ui.booking.repository.PurchaseResidenceRepository
import com.example.finalproject.ui.booking.viewModel.PurchaseResidenceViewModel
import kotlinx.android.synthetic.main.fragment_booked.number_booked_residences
import org.json.JSONException
import org.json.JSONObject

class BookedFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView

    private lateinit var bookedAdapter: BookedAdapter

    private lateinit var purchaseResidenceViewModel: PurchaseResidenceViewModel

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
        bookedAdapter = BookedAdapter(listOf())
        recyclerView.adapter = bookedAdapter

        initView()
    }

    private fun initView() {
        val repository = PurchaseResidenceRepository(RetrofitClient.instance)
        val factory = PurchaseResidenceFactory(repository)
        purchaseResidenceViewModel = ViewModelProvider(
            this@BookedFragment,
            factory
        )[PurchaseResidenceViewModel::class.java]

        purchaseResidenceViewModel.getPurchaseResidence(AppReferences.getToken(requireContext()))
        purchaseResidenceViewModel.purchaseResidenceResponseLiveData.observe(viewLifecycleOwner) {response ->
            response.let {
                bookedAdapter.updateData(it.residences)

                number_booked_residences.text = it.count.toString()
            }
        }

        purchaseResidenceViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        requireContext(), errorMessage, Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    Toast.makeText(
                        requireContext(), error, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}
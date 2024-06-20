package com.example.finalproject.ui.booking.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.booking.adapter.AcceptCancelBookedAdapter
import com.example.finalproject.ui.booking.factory.AcceptFactory
import com.example.finalproject.ui.booking.factory.CancelFactory
import com.example.finalproject.ui.booking.factory.GetBookedUsersFactory
import com.example.finalproject.ui.booking.models.BookedBy
import com.example.finalproject.ui.booking.repository.AcceptRepository
import com.example.finalproject.ui.booking.repository.CancelBookRepository
import com.example.finalproject.ui.booking.repository.GetBookedUsersRepository
import com.example.finalproject.ui.booking.viewModel.AcceptViewModel
import com.example.finalproject.ui.booking.viewModel.CancelViewModel
import com.example.finalproject.ui.booking.viewModel.GetBookedUsersViewModel
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.iv_empty_booked
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.recycle_booked
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.toolbar_accept_cancel_booked
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.tv_empty_booked_title_1
import kotlinx.android.synthetic.main.activity_accept_cancel_booked.tv_empty_booked_title_2
import org.json.JSONException
import org.json.JSONObject

class AcceptCancelBookedActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var bookedAdapter: AcceptCancelBookedAdapter

    private lateinit var getBookedUsersViewModel: GetBookedUsersViewModel

    private lateinit var acceptViewModel: AcceptViewModel

    private lateinit var cancelViewModel: CancelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_cancel_booked)

        setupActionBar()

        initRecyclerView()

        val x = intent.getStringExtra("Residence Id")
        Log.e("x" , x.toString())

        initView()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycle_booked)
        recyclerView.layoutManager = LinearLayoutManager(this@AcceptCancelBookedActivity ,
            LinearLayoutManager.VERTICAL , false)
        bookedAdapter = AcceptCancelBookedAdapter(
            mutableListOf(),
            this::onAcceptClick,
            this::onCancelClick
        )
        recyclerView.adapter = bookedAdapter
    }

    private fun initView() {
                                    /* Get-Booked-Users */

        val getBookedUsersRepository = GetBookedUsersRepository(RetrofitClient.instance)
        val getBookedUsersFactory = GetBookedUsersFactory(getBookedUsersRepository)
        getBookedUsersViewModel = ViewModelProvider(
            this@AcceptCancelBookedActivity, getBookedUsersFactory
        )[GetBookedUsersViewModel::class.java]

        getBookedUsers()

        val acceptRepository = AcceptRepository(RetrofitClient.instance)
        val acceptFactory = AcceptFactory(acceptRepository)
        acceptViewModel = ViewModelProvider(
            this@AcceptCancelBookedActivity, acceptFactory
        )[AcceptViewModel::class.java]

        acceptViewModel.acceptResponseLiveData.observe(this) { response ->
            response?.let {
                val status = response.status
                Log.e("Status", "Accept: $status")
                getBookedUsers()
            }
        }

        acceptViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, errorMessage, Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, error, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val cancelRepository = CancelBookRepository(RetrofitClient.instance)
        val cancelFactory = CancelFactory(cancelRepository)
        cancelViewModel = ViewModelProvider(
            this@AcceptCancelBookedActivity, cancelFactory
        )[CancelViewModel::class.java]

        cancelViewModel.cancelResponseLiveData.observe(this) { response ->
            response?.let {
                val status = response.status
                Log.e("Status", "Accept: $status")
                getBookedUsers()
            }
        }

        cancelViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, errorMessage, Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, error, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBookedUsers() {
        val token = AppReferences.getToken(this@AcceptCancelBookedActivity)
        val residenceId = intent.getStringExtra("Residence Id").toString()

        showProgressDialog(this@AcceptCancelBookedActivity, "please wait...")
        getBookedUsersViewModel.getBookedUsers(token, residenceId)

        getBookedUsersViewModel.getBookedUsersResponseLiveData.observe(
            this@AcceptCancelBookedActivity) { response ->
            hideProgressDialog()
            response.let {
                val status = response.status
                Log.e("Status", "GetBookedUsers: $status")

                bookedAdapter.list = it.bookedBy.toMutableList()
                bookedAdapter.notifyDataSetChanged()

                if (it.bookedBy.isEmpty()) {
                    iv_empty_booked.visibility = View.VISIBLE
                    tv_empty_booked_title_1.visibility = View.VISIBLE
                    tv_empty_booked_title_2.visibility = View.VISIBLE
                    recycle_booked.visibility = View.GONE
                } else {
                    iv_empty_booked.visibility = View.GONE
                    tv_empty_booked_title_1.visibility = View.GONE
                    tv_empty_booked_title_2.visibility = View.GONE
                    recycle_booked.visibility = View.VISIBLE
                }
            }
        }

        getBookedUsersViewModel.errorLiveData.observe(this) { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, errorMessage, Toast.LENGTH_LONG
                    ).show()
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@AcceptCancelBookedActivity, error, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onAcceptClick(bookedBy: BookedBy) {
        val token = AppReferences.getToken(this@AcceptCancelBookedActivity)
        val residenceId = intent.getStringExtra("Residence Id").toString()

        acceptViewModel.acceptBooking(token, residenceId, bookedBy._id)

        bookedAdapter.list.clear()
        bookedAdapter.notifyDataSetChanged()

        iv_empty_booked.visibility = View.VISIBLE
        tv_empty_booked_title_1.visibility = View.VISIBLE
        tv_empty_booked_title_2.visibility = View.VISIBLE
        recycle_booked.visibility = View.GONE

        Toast.makeText(this, "All bookings have been accepted and list is cleared!", Toast.LENGTH_SHORT).show()
    }

    private fun onCancelClick(bookedBy: BookedBy) {
        val token = AppReferences.getToken(this@AcceptCancelBookedActivity)
        val residenceId = intent.getStringExtra("Residence Id").toString()
        val userId = bookedBy._id

        cancelViewModel.cancelBooking(token, residenceId, userId)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_accept_cancel_booked)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_accept_cancel_booked.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
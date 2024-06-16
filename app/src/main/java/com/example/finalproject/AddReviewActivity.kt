package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.residence_details.activities.ResidenceDetailsActivity
import com.example.finalproject.ui.residence_details.factory.AddReviewFactory
import com.example.finalproject.ui.residence_details.factory.GetReviewsFactory
import com.example.finalproject.ui.residence_details.repository.AddReviewRepository
import com.example.finalproject.ui.residence_details.repository.GetReviewsRepository
import com.example.finalproject.ui.residence_details.viewModel.AddReviewViewModel
import com.example.finalproject.ui.residence_details.viewModel.GetReviewsViewModel
import kotlinx.android.synthetic.main.activity_add_review.btn_submit_review
import kotlinx.android.synthetic.main.activity_add_review.civ_add_review_agent
import kotlinx.android.synthetic.main.activity_add_review.et_add_review_detailed
import kotlinx.android.synthetic.main.activity_add_review.image_add_review
import kotlinx.android.synthetic.main.activity_add_review.rb_add_review
import kotlinx.android.synthetic.main.activity_add_review.toolbar_add_review
import kotlinx.android.synthetic.main.activity_add_review.tv_add_review_title_1
import kotlinx.android.synthetic.main.activity_add_review.tv_add_review_title_3
import org.json.JSONException
import org.json.JSONObject

class AddReviewActivity : BaseActivity() {

    private lateinit var getReviewsViewModel: GetReviewsViewModel

    private lateinit var networkUtils: NetworkUtils

    private lateinit var addReviewViewModel: AddReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("AddReviewActivity", "Residence ID: $residenceId")

        networkUtils = NetworkUtils(this@AddReviewActivity)

        initView()

        setUpActionBar()
    }

    private fun initView() {
                                    /* Get Reviews */

        val getReviewsRepository = GetReviewsRepository(RetrofitClient.instance)
        val factory = GetReviewsFactory(getReviewsRepository)
        getReviewsViewModel = ViewModelProvider(this, factory
        )[GetReviewsViewModel::class.java]

        getResidence()

                                    /* Add Review */

        val addReviewRepository = AddReviewRepository(RetrofitClient.instance)
        val addReviewFactory = AddReviewFactory(addReviewRepository)
        addReviewViewModel = ViewModelProvider(this@AddReviewActivity, addReviewFactory
        )[AddReviewViewModel::class.java]

        btn_submit_review.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                showProgressDialog(this@AddReviewActivity, "Adding Review...")
                addReview()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@AddReviewActivity)
            val id = intent.getStringExtra("residenceId")

            showProgressDialog(this@AddReviewActivity , "please wait...")
            getReviewsViewModel.getReviews(token, id!!)

            getReviewsViewModel.getReviewsResponseLiveData.observe(this@AddReviewActivity) {response ->
                hideProgressDialog()
                response.let {

                    if (response.reviews.isNotEmpty()) {
                        val residence = response.reviews[0].residenceId

                        if (residence.images.isNotEmpty()) {
                            Glide
                                .with(this)
                                .load(residence.images[0].url)
                                .into(image_add_review)
                        } else {
                            Log.e("UpdateResidenceActivity", "No images found for this residence")
                        }

                        val owner = response.reviews[0].residenceId.ownerId

                        if (owner.image.url.isNotEmpty()) {
                            Glide
                                .with(this)
                                .load(owner.image.url)
                                .into(civ_add_review_agent)
                        } else {
                            Log.e("UpdateResidenceActivity", "No images found for user")
                        }

                        tv_add_review_title_1.text = owner.username

                        tv_add_review_title_3.text = getString(R.string.how_was_experience, owner.username)
                    }
                }

                getReviewsViewModel.errorLiveData.observe(this@AddReviewActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(this@AddReviewActivity, errorMessage, Toast.LENGTH_LONG)
                                .show()

                            Log.e("AddReviewActivity", "Get Reviews Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@AddReviewActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun addReview() {
        val token = AppReferences.getToken(this@AddReviewActivity)
        val id = intent.getStringExtra("residenceId")

        val rating = rb_add_review.rating.toString()
        val comment = et_add_review_detailed.text.toString()

        addReviewViewModel.addReview(token, id!!, rating, comment)

        addReviewViewModel.addReviewLiveData.observe(this) { response ->
            hideProgressDialog()
            response.let {
                val message = response.message
                Log.e("AddReviewActivity", "Review Added: $message")

                val intent = Intent(this@AddReviewActivity, ResidenceDetailsActivity::class.java)
                intent.putExtra("residenceId", id)
                startActivity(intent)
            }
        }

        addReviewViewModel.errorLiveData.observe(this) { error ->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@AddReviewActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                    Log.e("AddReviewActivity", "Add Review Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@AddReviewActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_add_review)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_review.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
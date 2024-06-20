package com.example.finalproject.ui.residence_details.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.booking.factory.MakeBookFactory
import com.example.finalproject.ui.booking.repository.MakeBookRepository
import com.example.finalproject.ui.booking.viewModel.MakeBookViewModel
import com.example.finalproject.ui.residence_details.factory.PredictPriceFactory
import com.example.finalproject.ui.residence_details.repository.PredictPriceRepository
import com.example.finalproject.ui.residence_details.viewModel.PredictPriceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_predicted_price_details.*
import org.json.JSONException
import org.json.JSONObject

class PredictedPriceDetailsActivity : AppCompatActivity() {

    private lateinit var bookViewModel: MakeBookViewModel

    private lateinit var predictPriceViewModel: PredictPriceViewModel

    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predicted_price_details)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("residenceId", residenceId.toString())

        setUpViewModels()

        btn_show_predicted_price.setOnClickListener {
            predictPrice()
        }

        setUpActionBar()

    }

    private fun setUpViewModels() {
        val bookRepository = MakeBookRepository(RetrofitClient.instance)
        val bookFactory = MakeBookFactory(bookRepository)
        bookViewModel = ViewModelProvider(this, bookFactory
        )[MakeBookViewModel::class.java]

        val predictPriceRepository = PredictPriceRepository(RetrofitClient.instance)
        val predictPriceFactory = PredictPriceFactory(predictPriceRepository)
        predictPriceViewModel = ViewModelProvider(this, predictPriceFactory
        )[PredictPriceViewModel::class.java]
    }

    private fun predictPrice() {
        val token = AppReferences.getToken(this@PredictedPriceDetailsActivity)
        val residenceId = intent.getStringExtra("residenceId").toString()

        predictPriceViewModel.predictPrice(token, residenceId)

        predictPriceViewModel.predictPriceResponseLiveData.observe(this) { response ->
            response?.let {
                showPredictedPriceDialog(it.predicted_price.toString(), it.residence_price.toString())
            }
        }

        predictPriceViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(it).getString("message")
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showPredictedPriceDialog(predictedPrice: String, residencePrice: String) {
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        val editDialogView = layoutInflater.inflate(R.layout.predicted_price_dialog, null)
        editDialogView.background = ContextCompat.getDrawable(this, R.drawable.message_options_dialog_background)

        val btnContinue = editDialogView.findViewById<TextView>(R.id.btn_continue_predicted_price_details)
        val btnCancel = editDialogView.findViewById<TextView>(R.id.btn_cancel_predicted_price_details)

        val predictedPriceTextView = editDialogView.findViewById<TextView>(R.id.tv_predicted_price_details_2)
        predictedPriceTextView.text = "$ $predictedPrice"

        val residencePriceTextView = editDialogView.findViewById<TextView>(R.id.tv_residence_price_details_2)
        residencePriceTextView.text = "$ $residencePrice"

        btnContinue.setOnClickListener {
            bookNow()
            bottomSheetDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(editDialogView)
        bottomSheetDialog.show()
    }

    private fun bookNow() {
        val token = AppReferences.getToken(this@PredictedPriceDetailsActivity)
        val residenceId = intent.getStringExtra("residenceId").toString()

        bookViewModel.makeBook(token, residenceId)

        bookViewModel.makeBookResponseLiveData.observe(this) { response ->
            response?.let {
                val status = it.status
                Toast.makeText(this, "booked $status", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@PredictedPriceDetailsActivity, ResidenceDetailsActivity::class.java))
                finish()
            }
        }

        bookViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@PredictedPriceDetailsActivity, ResidenceDetailsActivity::class.java))
                    finish()
                } catch (e: JSONException) {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        bottomSheetDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog?.dismiss()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_book_now_predicted)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_book_now_predicted.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
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
import com.example.finalproject.ui.MainActivity
import com.example.finalproject.ui.booking.factory.MakeBookFactory
import com.example.finalproject.ui.booking.repository.MakeBookRepository
import com.example.finalproject.ui.booking.viewModel.MakeBookViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_predicted_price_details.btn_show_predicted_price
import kotlinx.android.synthetic.main.activity_predicted_price_details.toolbar_book_now_predicted
import org.json.JSONException
import org.json.JSONObject

class PredictedPriceDetailsActivity : AppCompatActivity() {

    private lateinit var bookViewModel: MakeBookViewModel

    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predicted_price_details)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("residenceId", residenceId.toString())

        btn_show_predicted_price.setOnClickListener {
            showPredictedPriceDialog()
        }

        setUpActionBar()
    }

    private fun showPredictedPriceDialog() {

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val editDialogView =
            layoutInflater.inflate(R.layout.predicted_price_dialog, null)
        editDialogView.background =
            ContextCompat.getDrawable(this, R.drawable.message_options_dialog_background)

        val btnContinue = editDialogView.findViewById<TextView>(R.id.btn_continue_predicted_price_details)
        val btnCancel = editDialogView.findViewById<TextView>(R.id.btn_cancel_predicted_price_details)

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
        val bookRepository = MakeBookRepository(RetrofitClient.instance)
        val factory = MakeBookFactory(bookRepository)
        bookViewModel = ViewModelProvider(
            this@PredictedPriceDetailsActivity,
            factory
        )[MakeBookViewModel::class.java]

        val token = AppReferences.getToken(this@PredictedPriceDetailsActivity)
        val residenceId = intent.getStringExtra("residenceId")

        bookViewModel.makeBook(token, residenceId.toString())

        bookViewModel.makeBookResponseLiveData.observe(this) {response ->
            response.let {
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
                    Toast.makeText(this@PredictedPriceDetailsActivity, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("UpdateResidenceActivity", "Update Error: $errorMessage")
                    startActivity(Intent(this@PredictedPriceDetailsActivity, ResidenceDetailsActivity::class.java))
                    finish()
                } catch (e: JSONException) {
                    Toast.makeText(this@PredictedPriceDetailsActivity, error, Toast.LENGTH_LONG).show()
                    Log.e("error is ", error)
                }
            }
        }
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
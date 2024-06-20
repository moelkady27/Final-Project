package com.example.finalproject.ui.residence_details.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalproject.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_predicted_price_details.btn_show_predicted_price
import kotlinx.android.synthetic.main.activity_predicted_price_details.toolbar_book_now_predicted

class PredictedPriceDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predicted_price_details)

        btn_show_predicted_price.setOnClickListener {
            showPredictedPriceDialog()
        }

        setUpActionBar()
    }

    private fun showPredictedPriceDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val editDialogView =
            layoutInflater.inflate(R.layout.predicted_price_dialog, null)
        editDialogView.background =
            ContextCompat.getDrawable(this, R.drawable.message_options_dialog_background)

        val btnContinue = editDialogView.findViewById<TextView>(R.id.btn_continue_predicted_price_details)
        val btnCancel = editDialogView.findViewById<TextView>(R.id.btn_cancel_predicted_price_details)

        btnContinue.setOnClickListener {

        }

        btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(editDialogView)
        bottomSheetDialog.show()
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
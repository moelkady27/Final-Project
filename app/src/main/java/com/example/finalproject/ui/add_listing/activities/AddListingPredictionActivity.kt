package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.finalproject.R
import com.example.finalproject.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_add_listing_prediction.change_price_predicted_price
import kotlinx.android.synthetic.main.activity_add_listing_prediction.finish_predicted_price

class AddListingPredictionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_prediction)

        initButtons()
    }

    private fun initButtons() {
        finish_predicted_price.setOnClickListener {
            val intent = Intent(
                this@AddListingPredictionActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        change_price_predicted_price.setOnClickListener {
            showChangePredictedPriceDialog()
        }
    }

    private fun showChangePredictedPriceDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val editDialogView =
            layoutInflater.inflate(R.layout.edit_predicted_price_dialog, null)
        editDialogView.background =
            ContextCompat.getDrawable(this, R.drawable.message_options_dialog_background)

        val editPriceButton = editDialogView.findViewById<ImageView>(R.id.iv_change_predicted_price_edit)
        val cancelButton = editDialogView.findViewById<TextView>(R.id.btn_cancel_change_predicted_price)


        editPriceButton.setOnClickListener {

        }

        cancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(editDialogView)
        bottomSheetDialog.show()
    }

}
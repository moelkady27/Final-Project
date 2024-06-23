package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.MainActivity
import com.example.finalproject.ui.add_listing.factory.GetPriceFactory
import com.example.finalproject.ui.add_listing.factory.UpdatePriceFactory
import com.example.finalproject.ui.add_listing.repository.GetPriceRepository
import com.example.finalproject.ui.add_listing.repository.UpdatePriceRepository
import com.example.finalproject.ui.add_listing.viewModel.GetPriceViewModel
import com.example.finalproject.ui.add_listing.viewModel.UpdatePriceViewModel
import com.example.finalproject.ui.residence_details.factory.PredictPriceFactory
import com.example.finalproject.ui.residence_details.repository.PredictPriceRepository
import com.example.finalproject.ui.residence_details.viewModel.PredictPriceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_listing_prediction.change_price_predicted_price
import kotlinx.android.synthetic.main.activity_add_listing_prediction.finish_predicted_price
import kotlinx.android.synthetic.main.activity_add_listing_prediction.tv_predicted_price_2
import org.json.JSONException
import org.json.JSONObject

class AddListingPredictionActivity : AppCompatActivity() {

    private lateinit var predictPriceViewModel: PredictPriceViewModel

    private lateinit var getPriceViewModel: GetPriceViewModel

    private lateinit var updatePriceViewModel: UpdatePriceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_prediction)

        initView()

        initButtons()

        val residenceId = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "prediction get $residenceId")

        val residence_Id = intent.getStringExtra("residence_Id").toString()
        Log.e("residence_Id", "prediction get $residence_Id")
    }

    private fun initView() {

                                //        predicted price

        val predictPriceRepository = PredictPriceRepository(RetrofitClient.instance)
        val predictPriceFactory = PredictPriceFactory(predictPriceRepository)
        predictPriceViewModel = ViewModelProvider(this, predictPriceFactory
        )[PredictPriceViewModel::class.java]

        val token = AppReferences.getToken(this@AddListingPredictionActivity)
        val residenceId = intent.getStringExtra("residenceId").toString()

        predictPriceViewModel.predictPrice(token, residenceId)

        predictPriceViewModel.predictPriceResponseLiveData.observe(this) { response ->
            response?.let {
                val predictedPrice = it.predicted_price.toString()
                tv_predicted_price_2.text = predictedPrice
            }
        }

        predictPriceViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(it).getString("message")
                    Log.e("errorMessage", it)
                } catch (e: JSONException) {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                    Log.e("error", it)
                }
            }
        }

                                    //        get price
        val getPriceRepository = GetPriceRepository(RetrofitClient.instance)
        val getPriceFactory = GetPriceFactory(getPriceRepository)
        getPriceViewModel = ViewModelProvider(this@AddListingPredictionActivity, getPriceFactory
        )[GetPriceViewModel::class.java]

                                //        update price

        val updatePriceRepository = UpdatePriceRepository(RetrofitClient.instance)
        val updatePriceFactory = UpdatePriceFactory(updatePriceRepository)
        updatePriceViewModel = ViewModelProvider(this@AddListingPredictionActivity, updatePriceFactory
        )[UpdatePriceViewModel::class.java]

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

        val newPriceEditText = editDialogView.findViewById<TextInputEditText>(R.id.et_change_predicted_price)

        val token = AppReferences.getToken(this@AddListingPredictionActivity)
        val residenceId = intent.getStringExtra("residenceId").toString()

        getPriceViewModel.getPrice(token, residenceId)

        getPriceViewModel.getPriceResponseLiveData.observe(this) { response ->
            response?.let {
                val salePrice = it.salePrice.toString()
                newPriceEditText.setText(salePrice)
            }
        }

        getPriceViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(it).getString("message")
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        editPriceButton.setOnClickListener {
            val newPrice = newPriceEditText.text.toString().toIntOrNull()
            if (newPrice != null) {
                val token = AppReferences.getToken(this@AddListingPredictionActivity)
                val residenceId = intent.getStringExtra("residenceId").toString()

                updatePriceViewModel.updatePrice(token, residenceId, newPrice)

                updatePriceViewModel.updatePriceResponseLiveData.observe(this) { response ->
                    response?.let {
                        val status = it.status
                        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                    }
                }

                updatePriceViewModel.errorLiveData.observe(this) { error ->
                    error?.let {
                        try {
                            val errorMessage = JSONObject(it).getString("message")
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setContentView(editDialogView)
        bottomSheetDialog.show()

        cancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
}
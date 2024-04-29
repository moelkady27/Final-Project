package com.example.finalproject.ui.complete_register.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.complete_register.factory.CompleteSignUpFactory
import com.example.finalproject.ui.complete_register.repository.CompleteSignUpRepository
import com.example.finalproject.ui.complete_register.viewModels.CompleteSignUpViewModel
import kotlinx.android.synthetic.main.activity_complete_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_first_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_gender
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_last_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_phone_number
import kotlinx.android.synthetic.main.activity_complete_sign_up.toolbar_complete_sign_up
import org.json.JSONException
import org.json.JSONObject

class CompleteSignUpActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var completeSignUpViewModel: CompleteSignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_sign_up)

        networkUtils = NetworkUtils(this)

        initView()

        setUpActionBar()
    }

    private fun initView(){
        val completeSignUpRepository = CompleteSignUpRepository(RetrofitClient.instance)
        val factoryCompleteSignUp = CompleteSignUpFactory(completeSignUpRepository)
        completeSignUpViewModel = ViewModelProvider(
            this@CompleteSignUpActivity, factoryCompleteSignUp)[CompleteSignUpViewModel::class.java]

        btn_next_sign_up.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                completeProfile()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_complete_sign_up)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_complete_sign_up.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun completeProfile() {
        val firstName = et_first_name.text.toString().trim()
        val lastName = et_last_name.text.toString().trim()
        val gender = et_gender.text.toString().trim()
        val phoneNumber = et_phone_number.text.toString().trim()

        val token = AppReferences.getToken(this@CompleteSignUpActivity)

        if (isValidInput()) {
            showProgressDialog(this@CompleteSignUpActivity , "completing registration...")
            completeSignUpViewModel.completeSignUp(token, firstName, lastName, gender, phoneNumber)

            completeSignUpViewModel.completeSignUpResponseLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("CompleteSignUpActivity", "Status: $status")

                    Toast.makeText(this@CompleteSignUpActivity, status, Toast.LENGTH_LONG).show()

                    startActivity(
                        Intent(
                            this@CompleteSignUpActivity,
                            UploadPhotoActivity::class.java
                        )
                    )
                }
            }

            completeSignUpViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@CompleteSignUpActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()

                        Log.e("CompleteSignUpActivity", "Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@CompleteSignUpActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val firstName = et_first_name.text.toString().trim()
        val lastName = et_last_name.text.toString().trim()
        val gender = et_gender.text.toString().trim()
        val phoneNumber = et_phone_number.text.toString().trim()

        if (firstName.isEmpty()) {
            et_first_name.error = "firstName is not allowed to be empty."
            return false
        } else if (!isFirstNameValid(firstName)) {
            et_first_name.error = "firstName length must be at least 2 characters long."
            return false
        }

        if (lastName.isEmpty()) {
            et_last_name.error = "lastName is not allowed to be empty."
            return false
        } else if (!isLastNameValid(lastName)) {
            et_last_name.error = "lastName length must be at least 2 characters long."
            return false
        }

        if (gender !in listOf("male", "female", "Male", "Female", "MALE", "FEMALE")) {
            et_gender.error = "Invalid gender. Please enter 'male' or 'female'."
            return false
        }

        if (phoneNumber.isEmpty()) {
            et_phone_number.error = "phone is not allowed to be empty."
            return false
        } else if (!isPhoneNumberValid(phoneNumber)) {
            et_phone_number.error = "Invalid phone number. Please enter a valid 11-digit number."
            return false
        }

        return true
    }

    private fun isFirstNameValid(firstName: CharSequence): Boolean {
        return firstName.length >= 2
    }

    private fun isLastNameValid(lastName: CharSequence): Boolean {
        return lastName.length >= 2
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11 && phoneNumber.all {
            it.isDigit()
        }
    }
}
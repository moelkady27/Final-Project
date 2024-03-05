package com.example.finalproject.ui.password.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.password.viewModels.ResetPasswordViewModel
import kotlinx.android.synthetic.main.activity_reset_password.btn_next_reset_password
import kotlinx.android.synthetic.main.activity_reset_password.et_confirm_password
import kotlinx.android.synthetic.main.activity_reset_password.et_password
import kotlinx.android.synthetic.main.activity_reset_password.toolbar_reset_password
import org.json.JSONException
import org.json.JSONObject

class ResetPasswordActivity : BaseActivity() {

    private lateinit var restPasswordViewModel: ResetPasswordViewModel

    private lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        networkUtils = NetworkUtils(this@ResetPasswordActivity)

        restPasswordViewModel = ViewModelProvider(this@ResetPasswordActivity).get(ResetPasswordViewModel::class.java)

        restPasswordViewModel.resetPasswordResponseLiveData.observe(
            this@ResetPasswordActivity, Observer { response ->
                hideProgressDialog()
                response.let {
                    val message = response.message

                    Toast.makeText(this@ResetPasswordActivity, message, Toast.LENGTH_LONG).show()

                    startActivity(Intent(this@ResetPasswordActivity, ResetPasswordSuccessActivity::class.java))
                }
        })

        restPasswordViewModel.errorLiveData.observe(this@ResetPasswordActivity, Observer { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@ResetPasswordActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("RestPasswordActivity", errorMessage)

                } catch (e: JSONException) {
                    Toast.makeText(this@ResetPasswordActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_next_reset_password.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                resetPassword()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        setUpActionBar()

    }

    private fun resetPassword() {
        val email = AppReferences.getUserEmail(this@ResetPasswordActivity)

        val password = et_password.text.toString().trim()
        val confirmPass = et_confirm_password.text.toString().trim()

        if (isValidInput()) {
            showProgressDialog(this@ResetPasswordActivity, "Resting Password...")
            restPasswordViewModel.resetPassword(email, password, confirmPass)
        }

    }

    private fun isValidInput(): Boolean {
        val password = et_password.text.toString().trim()
        val confirmPass = et_confirm_password.text.toString().trim()

        if (password.isEmpty()){
            et_password.error = "Password is not allowed to be empty."
            return false
        }
        if (confirmPass.isEmpty()){
            et_confirm_password.error = "Confirm Password is not allowed to be empty."
            return false
        }

        if (password!=confirmPass){
            et_confirm_password.error = "Passwords do not match"
        }
        return true
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_reset_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_reset_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
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
import com.example.finalproject.ui.password.viewModels.VerificationCodeForgetPasswordViewModel
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.btn_verify_code_forget
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.et_code_box_forget
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.toolbar_validation_forget
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.tv_Resend_forget
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.et_code_box
import org.json.JSONException
import org.json.JSONObject

class VerificationCodeForgetPasswordActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var verificationCodeForgetPasswordViewModel: VerificationCodeForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code_forget_password)

        networkUtils = NetworkUtils(this@VerificationCodeForgetPasswordActivity)

        val email = AppReferences.getUserEmail(this@VerificationCodeForgetPasswordActivity)

        Log.e("email verify" , email)

        verificationCodeForgetPasswordViewModel = ViewModelProvider(this).get(
            VerificationCodeForgetPasswordViewModel::class.java)

        verificationCodeForgetPasswordViewModel.verificationCodeForgetResponseLiveData.observe(
            this@VerificationCodeForgetPasswordActivity , Observer { response->
                hideProgressDialog()
                response.let {
                    val status = it.message

                    Log.e(
                        "VerificationCodeForgetPassActivity",
                        "Verification successful: Status - ${it.status}"
                    )

                    Toast.makeText(this, status, Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, ResetPasswordActivity::class.java))
                }
            })

        verificationCodeForgetPasswordViewModel.resendCodeForgetResponseLiveData.observe(
            this,
            Observer { response ->
                hideProgressDialog()
                response?.let {
                    Toast.makeText(
                        this@VerificationCodeForgetPasswordActivity,
                        "Resend Code Successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        verificationCodeForgetPasswordViewModel.errorLiveData.observe(this, Observer { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@VerificationCodeForgetPasswordActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("VerificationCodeSignUpActivity", "Resend Code Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@VerificationCodeForgetPasswordActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        setUpActionBar()

        btn_verify_code_forget.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                verifyCode()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        tv_Resend_forget.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                resendCode()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_validation_forget)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_validation_forget.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun verifyCode() {
        val verificationCodeString = et_code_box_forget?.text?.toString()
        val userEmail = AppReferences.getUserEmail(this@VerificationCodeForgetPasswordActivity)

        if (!verificationCodeString.isNullOrBlank()) {
            val verificationCode = verificationCodeString.toInt()

            showProgressDialog(this@VerificationCodeForgetPasswordActivity, "Verifying OTP...")
            verificationCodeForgetPasswordViewModel.verifyForget(verificationCode, userEmail)

        } else {
            Toast.makeText(this, "Verification code cannot be empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun resendCode() {
        val email = AppReferences.getUserEmail(this@VerificationCodeForgetPasswordActivity)

        showProgressDialog(this@VerificationCodeForgetPasswordActivity, "resending verifying code...")
        verificationCodeForgetPasswordViewModel.resendCodeForget(email)

    }
}

package com.example.finalproject.ui.register.activities

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
import com.example.finalproject.ui.complete_register.activities.CompleteSignUpActivity
import com.example.finalproject.ui.register.factory.VerificationCodeSignUpFactory
import com.example.finalproject.ui.register.repository.VerificationCodeSignUpRepository
import com.example.finalproject.ui.register.viewModels.VerificationCodeSignUpViewModel
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.btn_verify_code
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.et_code_box
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.toolbar_validation
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.tv_Resend
import org.json.JSONException
import org.json.JSONObject

class VerificationCodeSignUpActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var verificationCodeSignUpViewModel: VerificationCodeSignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code_sign_up)

        networkUtils = NetworkUtils(this)

        initView()

        setUpActionBar()
    }

    private fun initView() {
        val verificationCodeSignUpRepository = VerificationCodeSignUpRepository(RetrofitClient.instance)
        val factory = VerificationCodeSignUpFactory(verificationCodeSignUpRepository)
        verificationCodeSignUpViewModel = ViewModelProvider(this, factory)[VerificationCodeSignUpViewModel::class.java]

        btn_verify_code.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                verifyCode()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        tv_Resend.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                resendCode()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_validation)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_validation.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun verifyCode() {
        val verificationCodeString = et_code_box.text.toString()

        if (verificationCodeString.isNotBlank()) {
            val verificationCode = verificationCodeString.toInt()

            val token = AppReferences.getToken(this@VerificationCodeSignUpActivity)

            showProgressDialog(this@VerificationCodeSignUpActivity, "Verifying your self...")
            verificationCodeSignUpViewModel.verifyAccount(token, verificationCode)

            verificationCodeSignUpViewModel.verificationCodeSignUpResponseLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {

                    AppReferences.setLoginState(this, true)

                    val status = it.status

                    Log.e(
                        "VerificationCodeSignUpActivity",
                        "Verification successful: Status - ${it.status}"
                    )

                    Toast.makeText(this, status, Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, CompleteSignUpActivity::class.java))
                }
            }

            verificationCodeSignUpViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@VerificationCodeSignUpActivity,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("VerificationCodeSignUpActivity", "Resend Code Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@VerificationCodeSignUpActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        } else {
            Toast.makeText(this, "Verification code cannot be empty", Toast.LENGTH_LONG).show()
        }

    }

    private fun resendCode() {
        val token = AppReferences.getToken(this@VerificationCodeSignUpActivity)

        showProgressDialog(this@VerificationCodeSignUpActivity, "resending verifying code...")
        verificationCodeSignUpViewModel.resendCode(token)

        verificationCodeSignUpViewModel.resendCodeResponseLiveData.observe(this) { response ->
            hideProgressDialog()
            response?.let {
                Toast.makeText(
                    this@VerificationCodeSignUpActivity,
                    "Resend Code Successful",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        verificationCodeSignUpViewModel.errorLiveData.observe(this) { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(
                        this@VerificationCodeSignUpActivity,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("VerificationCodeSignUpActivity", "Resend Code Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(
                        this@VerificationCodeSignUpActivity,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
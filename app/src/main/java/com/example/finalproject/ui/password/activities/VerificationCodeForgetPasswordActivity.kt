package com.example.finalproject.ui.password.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.ResetPasswordActivity
import com.example.finalproject.ui.complete_register.activities.CompleteSignUpActivity
import com.example.finalproject.ui.password.viewModels.VerificationCodeForgetPasswordViewModel
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.btn_verify_code_forget
import kotlinx.android.synthetic.main.activity_verification_code_forget_password.toolbar_validation_forget
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.btn_verify_code
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.toolbar_validation
import org.json.JSONException
import org.json.JSONObject

class VerificationCodeForgetPasswordActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var verificationCodeForgetPasswordViewModel: VerificationCodeForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code_forget_password)

        networkUtils = NetworkUtils(this@VerificationCodeForgetPasswordActivity)

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

    }

    private fun verifyCode() {

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

}
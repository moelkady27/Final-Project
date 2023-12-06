package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.CompleteSignUpActivity
import com.example.finalproject.ui.register.viewModels.VerificationCodeSignUpViewModel
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.btn_verify_code
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.et_code_box
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.toolbar_validation
import kotlinx.android.synthetic.main.activity_verification_code_sign_up.tv_Resend

class VerificationCodeSignUpActivity : AppCompatActivity() {

    private lateinit var verificationCodeSignUpViewModel: VerificationCodeSignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code_sign_up)

        verificationCodeSignUpViewModel =
            ViewModelProvider(this).get(VerificationCodeSignUpViewModel::class.java)

        verificationCodeSignUpViewModel.verificationCodeSignUpResponseLiveData.observe(
            this,
            Observer { response ->
                response?.let {

                    Log.e("VerificationCodeSignUpActivity", "Verification successful: Status - ${it.status}")

                    Log.e("VerificationCodeSignUpActivity", "Message - ${it.message}")

                    Toast.makeText(this@VerificationCodeSignUpActivity, it.message, Toast.LENGTH_LONG).show()

                    AppReferences.setLoginState(this@VerificationCodeSignUpActivity, true)

                    startActivity(Intent(this@VerificationCodeSignUpActivity,CompleteSignUpActivity::class.java))
                }
            })

        verificationCodeSignUpViewModel.errorLiveData.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this@VerificationCodeSignUpActivity, it, Toast.LENGTH_LONG).show()
            }
        })

        btn_verify_code.setOnClickListener {
            verifyCode()
        }

        tv_Resend.setOnClickListener {

        }

        setUpActionBar()
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
        val userId = AppReferences.getUserId(this@VerificationCodeSignUpActivity)
        val verificationCode = et_code_box.text.toString().toInt()

        verificationCodeSignUpViewModel.verifyAccount(userId, verificationCode)
    }
}
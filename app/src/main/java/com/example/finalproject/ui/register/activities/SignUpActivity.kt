package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.ui.SignInActivity
import com.example.finalproject.ui.VerificationCodeActivity
import com.example.finalproject.ui.register.request.SignUpRequest
import com.example.finalproject.ui.register.viewModels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_confirm_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_email_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_username_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.tv_login

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        signUpViewModel.signUpResponseLiveData.observe(this@SignUpActivity, Observer { response ->
            response?.let {
                val message = it.message
                Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

                Log.e("SignUpActivity", "Response Message: $message")

                startActivity(Intent(this@SignUpActivity, VerificationCodeActivity::class.java))
                finish()
            }
        })

        signUpViewModel.errorLiveData.observe(this@SignUpActivity, Observer { error ->
            error?.let {
                Toast.makeText(this@SignUpActivity, it, Toast.LENGTH_LONG).show()
            }
        })

        tv_login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        btn_next_sign_up.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val userName = et_username_sign_up.text.toString().trim()
        val email = et_email_sign_up.text.toString().trim()
        val password = et_password_sign_up.text.toString().trim()
        val confirmPass = et_confirm_password_sign_up.text.toString().trim()

        signUpViewModel.signUp(userName, email, password, confirmPass)
    }

}
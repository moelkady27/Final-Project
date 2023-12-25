package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.register.viewModels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_confirm_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_email_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_username_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.tv_login
import org.json.JSONException
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        networkUtils = NetworkUtils(this)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        signUpViewModel.signUpResponseLiveData.observe(this@SignUpActivity, Observer { response ->
            hideProgressDialog()
            response?.let {
                val message = it.message
                Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()

                Log.e("SignUpActivity", "Response Message: $message")

                val userId = it.user._id

                val token = it.token

                AppReferences.setUserId(this@SignUpActivity, userId)
                AppReferences.setToken(this@SignUpActivity, token)

                Log.e("SignUpActivity", "Sign Up successful: userId - $userId")

                Log.e("SignUpActivity", "Sign Up successful: token - $token")

                startActivity(Intent(this@SignUpActivity, VerificationCodeSignUpActivity::class.java))
                finish()
            }
        })

        signUpViewModel.errorLiveData.observe(this@SignUpActivity, Observer { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(this@SignUpActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        tv_login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }

        btn_next_sign_up.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                register()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }

    private fun register() {
        val userName = et_username_sign_up.text.toString().trim()
        val email = et_email_sign_up.text.toString().trim()
        val password = et_password_sign_up.text.toString().trim()
        val confirmPass = et_confirm_password_sign_up.text.toString().trim()

//        signUpViewModel.signUp(userName, email, password, confirmPass)

        if (isValidInput()) {
            showProgressDialog(this@SignUpActivity, "Signing Up...")
            signUpViewModel.signUp(userName , email, password, confirmPass)
        }
    }

    private fun isValidInput(): Boolean {
        val userName = et_username_sign_up.text.toString().trim()
        val email = et_email_sign_up.text.toString().trim()
        val password = et_password_sign_up.text.toString().trim()
        val confirmPass = et_confirm_password_sign_up.text.toString().trim()

        val cbTermsAndConditions = findViewById<AppCompatCheckBox>(R.id.cb_terms_and_conditions)

        if (userName.isEmpty()) {
            et_username_sign_up.error = "Username is not allowed to be empty."
            return false
        } else if (!isUserNameValid(userName)) {
            et_username_sign_up.error = "Username must be at least 6 characters long."
            return false
        }

        if (email.isEmpty()) {
            et_email_sign_up.error = "Email is not allowed to be empty."
            return false
        } else if (!isValidEmail(email)) {
            et_email_sign_up.error = "Please enter a valid email address."
            return false
        }

        if (password.isEmpty()) {
            et_password_sign_up.error = "Password cannot be empty."
            return false
        } else if (!isPasswordValid(password)) {
            et_password_sign_up.error = "Password must be at least 8 characters long."
            return false
        }

        if (confirmPass.isEmpty()) {
            et_confirm_password_sign_up.error = "Confirm Password cannot be empty."
            return false
        } else if (password != confirmPass) {
            et_confirm_password_sign_up.error = "Passwords do not match."
            return false
        }

        if (!cbTermsAndConditions.isChecked) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isUserNameValid(userName: CharSequence): Boolean {
        return userName.length >= 6
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    private fun isPasswordValid(password: CharSequence): Boolean {
        return password.length >= 8
    }
}
package com.example.finalproject.ui.register.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.password.activities.ForgotPasswordActivity
import com.example.finalproject.ui.MainActivity
import com.example.finalproject.ui.register.factory.SignInViewModelFactory
import com.example.finalproject.ui.register.repository.SignInRepository
import com.example.finalproject.ui.register.viewModels.SignInViewModel
import kotlinx.android.synthetic.main.activity_sign_in.btn_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.et_email_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.et_password_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.tv_forget_password
import kotlinx.android.synthetic.main.activity_sign_in.tv_register_now
import org.json.JSONException
import org.json.JSONObject

class SignInActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var signInViewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        networkUtils = NetworkUtils(this)

        initView()
    }

    private fun initView() {
        val signInRepository = SignInRepository(RetrofitClient.instance)
        val factory = SignInViewModelFactory(signInRepository)
        signInViewModel = ViewModelProvider(this@SignInActivity, factory)[SignInViewModel::class.java]

        tv_register_now.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        tv_forget_password.setOnClickListener {
            startActivity(Intent(this@SignInActivity , ForgotPasswordActivity::class.java))
        }

        btn_sign_in.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                signIn()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }

    private fun signIn() {
        val email = et_email_sign_in.text.toString().trim()
        val password = et_password_sign_in.text.toString().trim()

        if (isValidInput()) {
            showProgressDialog(this@SignInActivity, "Signing in...")
            signInViewModel.signIn(email, password)

            signInViewModel.signInResponseLiveData.observe(this) { response ->
                hideProgressDialog()

                response?.let {

                    val token = it.token
                    val userId = it.userId

                    Log.e("SignInActivity", "Login successful: TokenSignUp - ${it.token}")

                    AppReferences.setToken(this@SignInActivity, token)

                    AppReferences.setLoginState(this@SignInActivity, true)

                    AppReferences.setUserId(this@SignInActivity, userId)

                    Log.e("SignInActivity", "User Id is $userId")

                    if (it.isVerified) {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent =
                            Intent(this@SignInActivity, VerificationCodeSignUpActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
            }

            signInViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()

                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@SignInActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        Toast.makeText(this@SignInActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun isValidInput(): Boolean {
        val email = et_email_sign_in.text.toString().trim()
        val password = et_password_sign_in.text.toString().trim()

        if (email.isEmpty()) {
            et_email_sign_in.error = "Email is not allowed to be empty."
            return false
        } else if (!isValidEmail(email)) {
            et_email_sign_in.error = "Please enter a valid email address."
            return false
        }

        if (password.isEmpty()) {
            et_password_sign_in.error = "Password cannot be empty."
            return false
        }

        return true
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

}



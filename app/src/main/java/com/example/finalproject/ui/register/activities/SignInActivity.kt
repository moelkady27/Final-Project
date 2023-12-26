package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import androidx.lifecycle.Observer
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.ForgotPasswordActivity
import com.example.finalproject.ui.MainActivity
import com.example.finalproject.ui.register.viewModels.SignInViewModel
import kotlinx.android.synthetic.main.activity_sign_in.btn_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.et_email_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.et_password_sign_in
import kotlinx.android.synthetic.main.activity_sign_in.tv_forget_password
import kotlinx.android.synthetic.main.activity_sign_in.tv_register_now
import kotlinx.android.synthetic.main.activity_sign_up.et_confirm_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_email_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_password_sign_up
import kotlinx.android.synthetic.main.activity_sign_up.et_username_sign_up
import org.json.JSONException
import org.json.JSONObject

class SignInActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var signInViewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        networkUtils = NetworkUtils(this)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        signInViewModel.signInResponseLiveData.observe(this, Observer { response ->
            hideProgressDialog()
            response?.let {

                val token = it.token

                Log.e("SignInActivity", "Login successful: TokenSignUp - ${it.token}")

                AppReferences.setToken(this@SignInActivity , token)

                AppReferences.setLoginState(this@SignInActivity, true)

                if (it.user.isVerified) {
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SignInActivity, VerificationCodeSignUpActivity::class.java)
                    startActivity(intent)
                }

                finish()
            }
        })

        signInViewModel.errorLiveData.observe(this, Observer { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@SignInActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(this@SignInActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

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



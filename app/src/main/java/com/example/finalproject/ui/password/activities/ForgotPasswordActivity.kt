package com.example.finalproject.ui.password.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.CheckYourMailActivity
import com.example.finalproject.ui.password.viewModels.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.btn_send
import kotlinx.android.synthetic.main.activity_forgot_password.et_email_forgot_password
import kotlinx.android.synthetic.main.activity_forgot_password.toolbar_forget_password
import kotlinx.android.synthetic.main.activity_sign_in.et_email_sign_in
import org.json.JSONObject

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    private lateinit var networkUtils: NetworkUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        networkUtils = NetworkUtils(this@ForgotPasswordActivity)

        forgotPasswordViewModel = ViewModelProvider(this@ForgotPasswordActivity).get(ForgotPasswordViewModel::class.java)

        forgotPasswordViewModel.forgotPasswordResponseLiveData.observe(this@ForgotPasswordActivity , Observer { response->
            hideProgressDialog()
            response.let {
                val message = response.message

                Toast.makeText(this@ForgotPasswordActivity , message , Toast.LENGTH_LONG).show()

                Log.e("status" , message)

                startActivity(Intent(this@ForgotPasswordActivity, CheckYourMailActivity::class.java))

            }
        })

        forgotPasswordViewModel.errorLiveDate.observe(this@ForgotPasswordActivity , Observer { error->
            hideProgressDialog()
            error.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@ForgotPasswordActivity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(this@ForgotPasswordActivity, "Error Server", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        setUpActionBar()

        btn_send.setOnClickListener {
            forgotPass()
        }

    }

    private fun forgotPass() {
        val email = et_email_forgot_password.text.toString().trim()

        if (isValidInput()){
            showProgressDialog(this@ForgotPasswordActivity ,"Sending OTP...")
            forgotPasswordViewModel.forgotPass(email)
        }
    }

    private fun isValidInput(): Boolean {
        val email = et_email_forgot_password.text.toString().trim()

        if (email.isEmpty()){
            et_email_forgot_password.error = "email is not allowed to be empty."
            return false
        } else if (!isValidEmail(email)) {
            et_email_forgot_password.error = "Please enter a valid email address."
            return false
        }

        return true
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_forget_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_forget_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }
}
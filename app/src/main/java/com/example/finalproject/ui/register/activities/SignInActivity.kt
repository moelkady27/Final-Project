package com.example.finalproject.ui.register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import androidx.lifecycle.Observer
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
import org.json.JSONException
import org.json.JSONObject

class SignInActivity : BaseActivity() {

    private lateinit var signInViewModel: SignInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        signInViewModel.signInResponseLiveData.observe(this, Observer { response ->
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
            signIn()
        }
    }


    private fun signIn() {
        val email = et_email_sign_in.text.toString().trim()
        val password = et_password_sign_in.text.toString().trim()

        showProgressDialog(this@SignInActivity , "Signing in...")
        signInViewModel.signIn(email, password)
    }
}


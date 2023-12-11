package com.example.finalproject.ui.register.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.register.viewModels.CompleteSignUpViewModel
import kotlinx.android.synthetic.main.activity_complete_sign_up.btn_next_sign_up
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_first_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_gender
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_last_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_phone_number
import kotlinx.android.synthetic.main.activity_complete_sign_up.toolbar_complete_sign_up
import org.json.JSONException
import org.json.JSONObject

class CompleteSignUpActivity : AppCompatActivity() {

    private lateinit var completeSignUpViewModel: CompleteSignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_sign_up)

        setUpActionBar()

        completeSignUpViewModel = ViewModelProvider(this).get(CompleteSignUpViewModel::class.java)

        completeSignUpViewModel.completeSignUpResponseLiveData.observe(this, Observer { response ->
            response?.let {
                val status = it.status

                Log.e("CompleteSignUpActivity", "Status: $status")

                Toast.makeText(this@CompleteSignUpActivity, status, Toast.LENGTH_LONG).show()

                startActivity(Intent(this@CompleteSignUpActivity, UploadPhotoActivity::class.java))
            }
        })

        completeSignUpViewModel.errorLiveData.observe(this, Observer { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@CompleteSignUpActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("CompleteSignUpActivity", "Complete Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@CompleteSignUpActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_next_sign_up.setOnClickListener {
            completeProfile()
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_complete_sign_up)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_complete_sign_up.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun completeProfile() {
        val firstName = et_first_name.text.toString().trim()
        val lastName = et_last_name.text.toString().trim()
        val gender = et_gender.text.toString().trim()
        val phoneNumber = et_phone_number.text.toString().trim()

        val token = AppReferences.getToken(this@CompleteSignUpActivity)

        completeSignUpViewModel.completeSignUp(token, firstName, lastName, gender, phoneNumber)


    }
}
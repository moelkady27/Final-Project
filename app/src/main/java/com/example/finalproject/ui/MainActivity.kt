package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.register.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.textView
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.logOutResponseLiveData.observe(this, Observer { response ->
            response.let {

                val message = it.status

                Log.e("MainActivity", "Status: $message")

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

                AppReferences.setLoginState(this@MainActivity, false)

                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }
        })

        mainViewModel.errorLiveData.observe(this, Observer { error ->
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("Logout", "Logout Error: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        textView.setOnClickListener {
            val token = AppReferences.getToken(this@MainActivity)

            if (token.isNotEmpty()) {
                Log.e("MainActivity", "Token: $token")
                mainViewModel.logout(token)
            } else {
                Log.e("MainActivity", "Token is empty or null")
            }
        }
    }
}
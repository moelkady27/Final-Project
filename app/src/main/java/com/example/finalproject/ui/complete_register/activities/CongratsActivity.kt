package com.example.finalproject.ui.complete_register.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.MainActivity
import kotlinx.android.synthetic.main.activity_congrats.btn_start_sign_up_success

class CongratsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        initView()
    }

    private fun initView(){
        btn_start_sign_up_success.setOnClickListener {
            AppReferences.setLoginState(this@CongratsActivity, true)

            val intent = Intent(this@CongratsActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }
}
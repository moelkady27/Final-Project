package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.example.finalproject.R
import com.example.finalproject.socket.SocketHandler
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.chat.models.MessageChatting
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {

    private lateinit var socketHandler: SocketHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        socketHandler = SocketHandler(this@SplashActivity)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            finish()
        }, 2500)


        socketHandler.connect { isConnected ->
            if (isConnected) {
                Log.e("Socket", "Socket connected")
            } else {
                Log.e("Socket", "Socket connection failed")
            }
        }

    }
}
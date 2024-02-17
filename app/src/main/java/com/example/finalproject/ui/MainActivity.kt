package com.example.finalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.finalproject.R
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.register.viewModels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // mahmaaaa

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = supportFragmentManager.findFragmentById(R.id.frag_host) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigation, navController.navController)
    }
}
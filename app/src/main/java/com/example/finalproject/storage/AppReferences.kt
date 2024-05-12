package com.example.finalproject.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object AppReferences {

    fun setLoginState(context: Activity?, state: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("login", state)
        editor.apply()
    }

    fun getLoginState(context: Activity?): Boolean {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("login", false)
    }

    fun setUserId(context: Activity?, userId: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    fun getUserId(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "")!!
    }

    fun setToken(context: Activity?, state: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("token", state)
        editor.apply()
    }

    fun getToken(context: Context?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("token", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "")!!
    }

    fun setUserEmail(context: Activity?, email: String) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("userEmail", email)
        editor.apply()
    }

    fun getUserEmail(context: Activity?): String {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userEmail", "")!!
    }

}
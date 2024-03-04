package com.example.finalproject.ui.password.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.password.viewModels.ChangePasswordViewModel
import com.example.finalproject.ui.password.viewModels.LogoutAllViewModel
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.setting.viewModels.LogOutViewModels
import kotlinx.android.synthetic.main.activity_change_password.btn_change_pass
import kotlinx.android.synthetic.main.activity_change_password.et_confirm_new_password
import kotlinx.android.synthetic.main.activity_change_password.et_new_password
import kotlinx.android.synthetic.main.activity_change_password.et_old_password
import kotlinx.android.synthetic.main.activity_change_password.toolbar_change_password
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_first_name
import kotlinx.android.synthetic.main.activity_delete_account.toolbar_delete_account
import kotlinx.android.synthetic.main.activity_forgot_password.toolbar_forget_password
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : BaseActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private lateinit var logOutAllViewModel: LogoutAllViewModel

    private lateinit var networkUtils: NetworkUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        networkUtils = NetworkUtils(this@ChangePasswordActivity)

        changePasswordViewModel = ViewModelProvider(this@ChangePasswordActivity).get(ChangePasswordViewModel::class.java)

        logOutAllViewModel = ViewModelProvider(this@ChangePasswordActivity).get(LogoutAllViewModel::class.java)


        changePasswordViewModel.changePasswordLiveData.observe(this@ChangePasswordActivity , Observer { response->
            hideProgressDialog()
            response.let {
                val status = response.status

                Toast.makeText(this@ChangePasswordActivity , status , Toast.LENGTH_LONG).show()

                AppReferences.setLoginState(this@ChangePasswordActivity , false)

                val token = AppReferences.getToken(this@ChangePasswordActivity)

                logOutAllViewModel.logoutAll(token)

                startActivity(Intent(this@ChangePasswordActivity , SignInActivity::class.java))
            }
        })

        changePasswordViewModel.changePasswordLiveData.observe(this@ChangePasswordActivity , Observer { response->
            hideProgressDialog()
            response.let {
                val status = response.status

                Toast.makeText(this@ChangePasswordActivity , status , Toast.LENGTH_LONG).show()

                if (status == "success") {
                    val token = AppReferences.getToken(this@ChangePasswordActivity)
                    logOutAllViewModel.logoutAll(token)
                    AppReferences.setLoginState(this@ChangePasswordActivity , false)
                    startActivity(Intent(this@ChangePasswordActivity , SignInActivity::class.java))
                }
            }
        })


        logOutAllViewModel.logOutAllResponseLiveData.observe(this@ChangePasswordActivity, Observer { response ->
            hideProgressDialog()
            response?.let {
                AppReferences.setLoginState(this@ChangePasswordActivity , false)
            }
        })

        logOutAllViewModel.errorLiveData.observe(this@ChangePasswordActivity, Observer { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@ChangePasswordActivity, errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(this@ChangePasswordActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_change_pass.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                changePass()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_change_password)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_change_password.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun changePass() {
        val token = AppReferences.getToken(this@ChangePasswordActivity)
        val oldPass = et_old_password.text.toString().trim()
        val newPass = et_new_password.text.toString().trim()
        val confirmNewPass = et_confirm_new_password.text.toString().trim()

        if (isValidInput()) {
            showProgressDialog(this@ChangePasswordActivity, "Changing Password...")
            changePasswordViewModel.changePassword(token , oldPass , newPass , confirmNewPass)
        }
    }

    private fun isValidInput(): Boolean {
        val oldPass = et_old_password.text.toString().trim()
        val newPass = et_new_password.text.toString().trim()
        val confirmNewPass = et_confirm_new_password.text.toString().trim()

        if (oldPass.isEmpty()){
            et_old_password.error = "Old password is not allowed to be empty."
            return false
        }
        if (newPass.isEmpty()){
            et_new_password.error = "New password is not allowed to be empty."
            return false
        }

        if (confirmNewPass.isEmpty()){
            et_confirm_new_password.error = "Confirm New password is not allowed to be empty."
            return false
        }
        if (newPass!=confirmNewPass){
            et_confirm_new_password.error = "Passwords do not match"
        }
        return true
    }
}
package com.example.finalproject.ui.setting.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.WelcomeActivity
import com.example.finalproject.ui.setting.factory.DeleteAccountFactory
import com.example.finalproject.ui.setting.repository.DeleteAccountRepository
import com.example.finalproject.ui.setting.viewModels.DeleteAccountViewModel
import kotlinx.android.synthetic.main.activity_delete_account.btn_delete
import kotlinx.android.synthetic.main.activity_delete_account.et_password_del
import kotlinx.android.synthetic.main.activity_delete_account.toolbar_delete_account
import org.json.JSONException
import org.json.JSONObject

class DeleteAccountActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var deleteAccountViewModel: DeleteAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        networkUtils = NetworkUtils(this)

        initView()

        setUpActionBar()
    }

    private fun initView(){
        val deleteAccountRepository = DeleteAccountRepository(RetrofitClient.instance)
        val factory = DeleteAccountFactory(deleteAccountRepository)
        deleteAccountViewModel = ViewModelProvider(
            this@DeleteAccountActivity, factory)[DeleteAccountViewModel::class.java]

        btn_delete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()){
                deleteAccount()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }


    private fun deleteAccount() {
        val token = AppReferences.getToken(this@DeleteAccountActivity)

        val password = et_password_del.text.toString().trim()

        if (invalidInput()){
            showProgressDialog(this@DeleteAccountActivity, "Deleting Account..")
            deleteAccountViewModel.deleteAccount(token, password)

            deleteAccountViewModel.deleteAccountResponseLiveData.observe(
                this@DeleteAccountActivity
            ) { response ->
                hideProgressDialog()
                response.let {
                    val status = response.status

                    Toast.makeText(this@DeleteAccountActivity, status, Toast.LENGTH_LONG).show()

                    AppReferences.setLoginState(this@DeleteAccountActivity, false)

                    startActivity(Intent(this@DeleteAccountActivity, WelcomeActivity::class.java))
                }
            }

            deleteAccountViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@DeleteAccountActivity, errorMessage, Toast.LENGTH_LONG)
                            .show()

                        Log.e("DeleteAccountActivity", errorMessage)

                    } catch (e: JSONException) {
                        Toast.makeText(this@DeleteAccountActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun invalidInput(): Boolean {
        val password = et_password_del.text.toString().trim()

        if(password.isEmpty()){
            et_password_del.error = "Password cannot be empty"
            return false
        }
        return true
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_delete_account)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_delete_account.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
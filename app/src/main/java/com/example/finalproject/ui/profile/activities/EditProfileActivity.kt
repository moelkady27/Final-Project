package com.example.finalproject.ui.profile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.MainActivity
import com.example.finalproject.ui.WelcomeActivity
import com.example.finalproject.ui.password.activities.ChangePasswordActivity
import com.example.finalproject.ui.profile.fragment.ProfileFragment
import com.example.finalproject.ui.profile.viewModels.EditProfileViewModel
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_first_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_gender
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_last_name
import kotlinx.android.synthetic.main.activity_complete_sign_up.et_phone_number
import kotlinx.android.synthetic.main.activity_edit_profile.btn_update_edit
import kotlinx.android.synthetic.main.activity_edit_profile.edt_first_name
import kotlinx.android.synthetic.main.activity_edit_profile.edt_gender
import kotlinx.android.synthetic.main.activity_edit_profile.edt_last_name
import kotlinx.android.synthetic.main.activity_edit_profile.edt_phone_number
import kotlinx.android.synthetic.main.activity_edit_profile.edt_user_name
import kotlinx.android.synthetic.main.activity_edit_profile.ib_upload_preview
import kotlinx.android.synthetic.main.activity_edit_profile.toolbar_edit_profile
import org.json.JSONException
import org.json.JSONObject

class EditProfileActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

    private lateinit var editProfileViewModel: EditProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        networkUtils = NetworkUtils(this@EditProfileActivity)

        getUserInfoViewModel = ViewModelProvider(this@EditProfileActivity).get(GetUserInfoViewModel::class.java)

        getUserInfoViewModel.getUserInfoResponseLiveData.observe(this@EditProfileActivity , Observer { response->
            hideProgressDialog()
            response.let {
                val message = response.status

                val firstName = response.user.firstName
                val lastName = response.user.lastName
                val gender = response.user.gender
                val username = response.user.username
                val phoneNumber = response.user.phone
                val image = response.user.image.url

                Log.e("message" , message)

                edt_first_name.setText(firstName)
                edt_gender.setText(gender)
                edt_last_name.setText(lastName)
                edt_user_name.setText(username)
                edt_phone_number.setText(phoneNumber)

                Glide
                    .with(this@EditProfileActivity)
                    .load(image)
                    .centerCrop()
                    .into(ib_upload_preview)
            }
        })

        getUserInfoViewModel.errorLiveData.observe(this@EditProfileActivity , Observer { error->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@EditProfileActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("EditProfileActivity", "Error Update: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@EditProfileActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        getUserInfo()

        editProfileViewModel = ViewModelProvider(this@EditProfileActivity).get(EditProfileViewModel::class.java)

        editProfileViewModel.editProfileResponseLiveData.observe(this@EditProfileActivity , Observer { response->
            hideProgressDialog()
            response.let {
                val message = response.message

                Toast.makeText(this@EditProfileActivity , message ,Toast.LENGTH_LONG).show()

                startActivity(Intent(this@EditProfileActivity , MainActivity::class.java))
            }
        })

        editProfileViewModel.errorLiveData.observe(this@EditProfileActivity , Observer { error->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@EditProfileActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("EditProfileActivity", "Error Update: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(this@EditProfileActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_update_edit.setOnClickListener {
            if (networkUtils.isNetworkAvailable()){
                updateProfile()
            }
            else{
                showErrorSnackBar("No internet connection", true)
            }
        }

        setUpActionBar()
    }

    private fun getUserInfo() {
        getUserInfoViewModel.getUserInfo(AppReferences.getToken(this@EditProfileActivity))

    }

    private fun updateProfile() {
        val token = AppReferences.getToken(this@EditProfileActivity)
        val firstName = edt_first_name.text.toString().trim()
        val gender = edt_gender.text.toString().trim()
        val username = edt_user_name.text.toString().trim()
        val lastName = edt_last_name.text.toString().trim()
        val phone = edt_phone_number.text.toString().trim()

        if (isValidInput()){
            showProgressDialog(this@EditProfileActivity , "Updating Profile..")
            editProfileViewModel.editProfile(token, firstName, gender, username, lastName, phone)
        }
    }

    private fun isValidInput(): Boolean {
        val firstName = edt_first_name.text.toString().trim()
        val gender = edt_gender.text.toString().trim()
        val username = edt_user_name.text.toString().trim()
        val lastName = edt_last_name.text.toString().trim()
        val phone = edt_phone_number.text.toString().trim()

        if (firstName.isEmpty()) {
            edt_first_name.error = "firstName is not allowed to be empty."
            return false
        } else if (!isFirstNameValid(firstName)) {
            edt_first_name.error = "firstName length must be at least 2 characters long."
            return false
        }

        if (username.isEmpty()) {
            edt_user_name.error = "username is not allowed to be empty."
            return false
        } else if (!isUserNameValid(username)) {
            edt_user_name.error = "userName length must be at least 2 characters long."
            return false
        }

        if (lastName.isEmpty()) {
            edt_last_name.error = "lastName is not allowed to be empty."
            return false
        } else if (!isLastNameValid(lastName)) {
            edt_last_name.error = "lastName length must be at least 2 characters long."
            return false
        }

        if (gender !in listOf("male", "female", "Male", "Female", "MALE", "FEMALE")) {
            edt_gender.error = "Invalid gender. Please enter 'male' or 'female'."
            return false
        }

        if (phone.isEmpty()) {
            edt_phone_number.error = "phone is not allowed to be empty."
            return false
        } else if (!isPhoneNumberValid(phone)) {
            edt_phone_number.error = "Invalid phone number. Please enter a valid 11-digit number."
            return false
        }

        return true
    }

    private fun isFirstNameValid(firstName: CharSequence): Boolean {
        return firstName.length >= 2
    }

    private fun isLastNameValid(lastName: CharSequence): Boolean {
        return lastName.length >= 2
    }

    private fun isUserNameValid(userName: CharSequence): Boolean {
        return userName.length >= 2
    }
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11 && phoneNumber.all {
            it.isDigit()
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_edit_profile)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_edit_profile.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
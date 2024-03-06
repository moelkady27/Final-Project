package com.example.finalproject.ui.profile.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_edit_profile.floatingActionButton_delete
import kotlinx.android.synthetic.main.activity_edit_profile.ib_upload_preview
import kotlinx.android.synthetic.main.activity_edit_profile.toolbar_edit_profile
import kotlinx.android.synthetic.main.activity_edit_profile.tv_change_profile_picture
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class EditProfileActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

    private lateinit var editProfileViewModel: EditProfileViewModel

    private lateinit var selectedImage: String

    private var REQUEST_CODE_GALLERY = 0

    private val pickImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImage = getPathFromUri(uri)
                    Log.e("image", selectedImage)

                    Glide.with(this@EditProfileActivity)
                        .load(selectedImage)
                        .centerCrop()
                        .into(ib_upload_preview)
                }
            }
        }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) } ?: ""
        cursor?.close()
        return path
    }

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

                val defaultImage = "https://res.cloudinary.com/dgslxtxg8/image/upload/v1703609152/iwonvcvpn6oidmyhezvh.jpg"

                if (image == defaultImage){
                    floatingActionButton_delete.visibility = View.GONE
                }
                else{
                    floatingActionButton_delete.visibility = View.VISIBLE
                }
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

        editProfileViewModel.deleteProfileImageResponseLiveData.observe(
            this@EditProfileActivity, Observer { response ->
                hideProgressDialog()
                response.let {
                    val status = response.status

                    Log.e("Delete Profile Image", status)
                }
            })

        editProfileViewModel.changeProfileImageResponseLiveData.observe(
            this@EditProfileActivity , Observer { response->
                hideProgressDialog()
                response.let {
                    val imageUrl = response.image.url

                    Log.e("image url" , selectedImage)
                }
            }
        )

        btn_update_edit.setOnClickListener {
            if (networkUtils.isNetworkAvailable()){
                changePhoto()
                updateProfile()
            }
            else{
                showErrorSnackBar("No internet connection", true)
            }
        }

        floatingActionButton_delete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()){
                deleteProfileImage()
                val defaultImage = "https://res.cloudinary.com/dgslxtxg8/image/upload/v1703609152/iwonvcvpn6oidmyhezvh.jpg"
                Glide
                    .with(this@EditProfileActivity)
                    .load(defaultImage)
                    .centerCrop()
                    .into(ib_upload_preview)
                floatingActionButton_delete.visibility = View.GONE
            }
            else{
                showErrorSnackBar("No internet connection", true)
            }
        }

        tv_change_profile_picture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@EditProfileActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@EditProfileActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
                return@setOnClickListener
            }
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageFromGallery.launch(galleryIntent)
        }

        setUpActionBar()
    }

    private fun deleteProfileImage() {
        editProfileViewModel.deleteProfileImage(AppReferences.getToken(this@EditProfileActivity))
    }

    private fun getUserInfo() {
        getUserInfoViewModel.getUserInfo(AppReferences.getToken(this@EditProfileActivity))

    }

    private fun changePhoto() {
        if (selectedImage != null) {
            val file = File(selectedImage)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val token = AppReferences.getToken(this@EditProfileActivity)

            editProfileViewModel.changeImage(token, body)

        } else {
            Toast.makeText(
                this@EditProfileActivity,
                "Please select an image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageFromGallery.launch(galleryIntent)
            } else {
                Toast.makeText(this@EditProfileActivity, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
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
package com.example.finalproject.ui.complete_register.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.complete_register.factory.UploadPhotoFactory
import com.example.finalproject.ui.complete_register.repository.UploadPhotoRepository
import com.example.finalproject.ui.complete_register.viewModels.UploadPhotoViewModel
import kotlinx.android.synthetic.main.activity_upload_photo.btn_next_upload_photo
import kotlinx.android.synthetic.main.activity_upload_photo.fl_from_gallery
import kotlinx.android.synthetic.main.activity_upload_photo.skip
import kotlinx.android.synthetic.main.activity_upload_photo.toolbar_upload_photo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class UploadPhotoActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var selectedImage: String

    private lateinit var uploadPhotoViewModel: UploadPhotoViewModel

    private var REQUEST_CODE_GALLERY = 0

    private val pickImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImage = getPathFromUri(uri)
                    Log.e("image", selectedImage)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo)

        networkUtils = NetworkUtils(this)

        initView()

        setUpActionBar()
    }

    private fun initView(){
                                    /* Upload-Photo */

        val uploadPhotoRepository = UploadPhotoRepository(RetrofitClient.instance)
        val factoryUploadPhoto = UploadPhotoFactory(uploadPhotoRepository)
        uploadPhotoViewModel = ViewModelProvider(
            this@UploadPhotoActivity, factoryUploadPhoto)[UploadPhotoViewModel::class.java]

        btn_next_upload_photo.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                uploadPhoto()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

                                    /* Gallery-Button */

//        fl_from_gallery.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(this@UploadPhotoActivity,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this@UploadPhotoActivity,
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
//                return@setOnClickListener
//            }
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            pickImageFromGallery.launch(galleryIntent)
//        }

        fl_from_gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            pickImageFromGallery.launch(intent)
        }

                                    /* Skip-Upload-Photo-Button */

        skip.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {

                val intent = Intent(this@UploadPhotoActivity , UploadPreviewActivity::class.java)
                val imageDef = "https://res.cloudinary.com/dgslxtxg8/image/upload/v1703609152/iwonvcvpn6oidmyhezvh.jpg"

                intent.putExtra("imageDef", imageDef)

                startActivity(intent)

            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }

    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_upload_photo)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_upload_photo.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun uploadPhoto() {
        if (selectedImage != null) {
            val file = File(selectedImage)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val token = AppReferences.getToken(this@UploadPhotoActivity)

            showProgressDialog(this@UploadPhotoActivity , "Uploading your image...")

            uploadPhotoViewModel.uploadPhoto(token, body)

            uploadPhotoViewModel.uploadPhotoResponseLiveData.observe(this, Observer { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Toast.makeText(this@UploadPhotoActivity, status, Toast.LENGTH_LONG).show()

                    Log.e("status" , status)

                    val intent = Intent(this@UploadPhotoActivity, UploadPreviewActivity::class.java)

                    intent.putExtra("imageUri", selectedImage)

                    Log.e("image is " , selectedImage)

                    startActivity(intent)

                }
            })

            uploadPhotoViewModel.errorLiveData.observe(this, Observer { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@UploadPhotoActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("UploadImageSignUpActivity", "Upload ImageSignUp Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(this@UploadPhotoActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                this@UploadPhotoActivity,
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
                Toast.makeText(this@UploadPhotoActivity, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show()
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

}
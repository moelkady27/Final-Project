package com.example.finalproject.ui.add_listing.activities

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.adapter.AddListingPhotosAdapter
import com.example.finalproject.ui.add_listing.factory.AddPhotoResidenceFactory
import com.example.finalproject.ui.add_listing.models.Image
import com.example.finalproject.ui.add_listing.repository.AddPhotoResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.AddPhotoResidenceViewModel
import kotlinx.android.synthetic.main.activity_add_listing_photos.button_next_listing_photos
import kotlinx.android.synthetic.main.activity_add_listing_photos.toolbar_add_listing_photos
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class AddListingPhotosActivity : BaseActivity() {

    private lateinit var addPhotoResidenceViewModel: AddPhotoResidenceViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddListingPhotosAdapter

    private lateinit var selectedImage: String

    private var REQUEST_CODE_GALLERY = 0


    private val imageList = arrayListOf(
        Image("", "", "", isAddButton = true)
    )

    private val pickImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImage = getPathFromUri(uri)
                    adapter.addImage(selectedImage)
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
        setContentView(R.layout.activity_add_listing_photos)

        initView()

        setUpActionBar()
    }

    private fun initView() {

        val id = intent.getStringExtra("lol").toString()
        Log.e("residenceId" , "photo is $id")

        val addPhotoResidenceRepository = AddPhotoResidenceRepository(RetrofitClient.instance)
        val factory = AddPhotoResidenceFactory(addPhotoResidenceRepository)
        addPhotoResidenceViewModel = ViewModelProvider(
            this,
            factory
        )[AddPhotoResidenceViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView_add_listing_photos)

        adapter = AddListingPhotosAdapter(imageList) {
            openImagePicker()
        }

        button_next_listing_photos.setOnClickListener {
            for (image in imageList) {
                if (image.url.isNotEmpty()) {
                    Log.e("ImageList", "Image: ${image.url}")
                    uploadImages()
                }
            }
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_add_listing_photos)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_listing_photos.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun openImagePicker() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_CODE_GALLERY
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageFromGallery.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageFromGallery.launch(galleryIntent)
            } else {
                Toast.makeText(this@AddListingPhotosActivity, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImages() {
        if (selectedImage != null) {
            val file = File(selectedImage)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("images", file.name, requestFile)
            val token = AppReferences.getToken(this@AddListingPhotosActivity)
            val residenceId = "66423767bb3bce1ac1356aad"

            addPhotoResidenceViewModel.uploadResidencePhoto(token, residenceId, body)

            showProgressDialog(this@AddListingPhotosActivity, "Uploading your images...")

            addPhotoResidenceViewModel.uploadPhotoResponseLiveData.observe(this@AddListingPhotosActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("status", status)

                    startActivity(Intent(this@AddListingPhotosActivity, FirstCompleteActivity::class.java))
                }
            }

            addPhotoResidenceViewModel.errorLiveData.observe(this@AddListingPhotosActivity) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@AddListingPhotosActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("UploadImageSignUpActivity", "Full error response: $error")

                    } catch (e: JSONException) {
                        Toast.makeText(this@AddListingPhotosActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(
                this@AddListingPhotosActivity,
                "Please select an image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
package com.example.finalproject.ui.update_listing.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.factory.AddPhotoResidenceFactory
import com.example.finalproject.ui.add_listing.repository.AddPhotoResidenceRepository
import com.example.finalproject.ui.add_listing.viewModel.AddPhotoResidenceViewModel
import com.example.finalproject.ui.complete_register.activities.MapActivity
import com.example.finalproject.ui.update_listing.adapter.UpdateListingPhotosAdapter
import com.example.finalproject.ui.update_listing.factory.DeleteResidenceImageFactory
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.factory.UpdateResidenceFactory
import com.example.finalproject.ui.update_listing.models.Image
import com.example.finalproject.ui.update_listing.repository.DeleteResidenceImageRepository
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.repository.UpdateResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.DeleteResidenceImageViewModel
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.example.finalproject.ui.update_listing.viewModel.UpdateResidenceViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_update_residence.apartment_location_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.apartment_name_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.apartment_price_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.btn_next_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.chip_apartment_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_cottage_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_hotel_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_house_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_rent_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_sell_update
import kotlinx.android.synthetic.main.activity_update_residence.chip_villa_update
import kotlinx.android.synthetic.main.activity_update_residence.et_home_name_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.image_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.select_map_update_listing
import kotlinx.android.synthetic.main.activity_update_residence.toolbar_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.tv_apartment_update_residence
import kotlinx.android.synthetic.main.activity_update_residence.tv_update_residence_3
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class UpdateResidenceActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var addPhotoResidenceViewModel: AddPhotoResidenceViewModel

    private lateinit var deleteResidenceImageViewModel: DeleteResidenceImageViewModel

    private lateinit var updateResidenceViewModel: UpdateResidenceViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: UpdateListingPhotosAdapter

    private lateinit var selectedImage: String

    private var REQUEST_CODE_GALLERY = 0

//    private var REQUEST_CODE_MAP = 1

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
        setContentView(R.layout.activity_update_residence)

        recyclerView = findViewById(R.id.recyclerView_update_listing_photos)
        recyclerView.setHasFixedSize(true)
        adapter = UpdateListingPhotosAdapter(imageList,
            onAddImageClick = {
                openImagePicker()
            },
            onDeleteImageClick = { imageId ->
                deleteResidenceImage(imageId)
            }
        )

        val residenceId = intent.getStringExtra("residence_id")
        Log.e("Residence ID", residenceId.toString())


        networkUtils = NetworkUtils(this@UpdateResidenceActivity)

        initChips()

        initView()

        setUpActionBar()
    }

    private fun initView() {
//        ********************* getting residence ****************

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@UpdateResidenceActivity,
            factoryGetResidence
        )[GetResidenceViewModel::class.java]

        getResidence()

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

//        ********************* photo residence ****************

        val addPhotoResidenceRepository = AddPhotoResidenceRepository(RetrofitClient.instance)
        val factory = AddPhotoResidenceFactory(addPhotoResidenceRepository)
        addPhotoResidenceViewModel = ViewModelProvider(
            this,
            factory
        )[AddPhotoResidenceViewModel::class.java]

        //        ********************* update residence ****************

        val updateResidenceRepository = UpdateResidenceRepository(RetrofitClient.instance)
        val factoryUpdateResidence = UpdateResidenceFactory(updateResidenceRepository)
        updateResidenceViewModel = ViewModelProvider(
            this@UpdateResidenceActivity, factoryUpdateResidence
        )[UpdateResidenceViewModel::class.java]

        btn_next_update_listing.setOnClickListener {
            for (image in imageList) {
                if (image.url.isNotEmpty()) {
                    Log.e("ImageList", "Image: ${image.url}")
                    uploadImages(image.url)
                }
            }

            val title = et_home_name_update_listing.text.toString()
            val type = if (chip_rent_update.isChecked) "rent" else "sale"
            val category = when {
                chip_house_update.isChecked -> "house"
                chip_apartment_update.isChecked -> "apartment"
                chip_hotel_update.isChecked -> "hotel"
                chip_villa_update.isChecked -> "villa"
                chip_cottage_update.isChecked -> "cottage"
                else -> ""
            }

            if (isValidInput()) {
                showProgressDialog(this@UpdateResidenceActivity , "please wait...")
                val token = AppReferences.getToken(this@UpdateResidenceActivity)
                val residenceId = intent.getStringExtra("residence_id").toString()
                updateResidenceViewModel.updateResidence(token, residenceId, title, type, category)

                updateResidenceViewModel.updateResidenceResponseLiveData.observe(this@UpdateResidenceActivity) { response ->
                    hideProgressDialog()
                    response?.let {
                        val status = it.status
                        Log.e("status", status)
                    }
                }

                updateResidenceViewModel.errorLiveData.observe(this@UpdateResidenceActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(this@UpdateResidenceActivity, errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("UpdateResidenceActivity", "Update Error: $errorMessage")
                        } catch (e: JSONException) {
                            Toast.makeText(this@UpdateResidenceActivity, error, Toast.LENGTH_LONG).show()
                            Log.e("error is ", error)
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this@UpdateResidenceActivity, FirstUpdateActivity::class.java)
            startActivity(intent)

        }

        //        ********************* delete photo residence ****************

        val deleteResidenceImageRepository = DeleteResidenceImageRepository(RetrofitClient.instance)
        val factoryDeleteResidenceImage =
            DeleteResidenceImageFactory(deleteResidenceImageRepository)
        deleteResidenceImageViewModel = ViewModelProvider(
            this@UpdateResidenceActivity,
            factoryDeleteResidenceImage
        )[DeleteResidenceImageViewModel::class.java]

//        select_map_update_listing.setOnClickListener {
//            val mapIntent = Intent(this@UpdateResidenceActivity, MapActivity::class.java)
//            val residenceLocation = getResidenceViewModel.getResidenceResponseLiveData.value?.residence?.location
//            mapIntent.putExtra("latitude", residenceLocation?.coordinates?.get(0))
//            mapIntent.putExtra("longitude", residenceLocation?.coordinates?.get(1))
//            startActivityForResult(mapIntent, REQUEST_CODE_MAP)
//        }
    }

    private fun isValidInput(): Boolean {
        val title = et_home_name_update_listing.text.toString()
        val type = if (chip_rent_update.isChecked) "rent" else "sale"
        val category = when {
            chip_house_update.isChecked -> "house"
            chip_apartment_update.isChecked -> "apartment"
            chip_hotel_update.isChecked -> "hotel"
            chip_villa_update.isChecked -> "villa"
            chip_cottage_update.isChecked -> "cottage"
            else -> ""
        }

        if (title.isEmpty()) {
            et_home_name_update_listing.error = "Title is required"
            return false
        }

        if (type.isEmpty()) {
            Toast.makeText(this, "Please select type", Toast.LENGTH_SHORT).show()
            return false
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@UpdateResidenceActivity)
            val id = intent.getStringExtra("residence_id").toString()

            showProgressDialog(this@UpdateResidenceActivity , "please wait...")
            getResidenceViewModel.getResidence(token, id)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@UpdateResidenceActivity) {response ->
                hideProgressDialog()
                response.let {

                    if (response.residence.images.isNotEmpty()) {
                        Glide
                            .with(this)
                            .load(response.residence.images[0].url)
                            .into(image_update_residence)
                    } else {
                        Log.e("UpdateResidenceActivity", "No images found for this residence")
                    }

                    tv_apartment_update_residence.text = response.residence.category
//                    number_star_update_residence
                    apartment_name_update_residence.text = response.residence.title
                    apartment_location_update_residence.text = response.residence.location.fullAddress
                    apartment_price_update_residence.text = response.residence.salePrice.toString()
                    tv_update_residence_3.text = response.residence.paymentPeriod
//                    iv_update_residence_fav

                    et_home_name_update_listing.setText(response.residence.title)

                    when (response.residence.type) {
                        "rent" -> {
                            chip_rent_update.isChecked = true
                            chip_sell_update.isChecked = false
                        }
                        "sale" -> {
                            chip_sell_update.isChecked = true
                            chip_rent_update.isChecked = false
                        }
                    }

                    when (response.residence.category) {
                        "house" -> chip_house_update.isChecked = true
                        "apartment" -> chip_apartment_update.isChecked = true
                        "hotel" -> chip_hotel_update.isChecked = true
                        "villa" -> chip_villa_update.isChecked = true
                        "cottage" -> chip_cottage_update.isChecked = true
                    }

                    if (response.residence.images.isNotEmpty()) {
                        imageList.clear()
                        for (image in response.residence.images) {
                            imageList.add(Image(image._id, image.url, image.url, isAddButton = false))
                        }
                        imageList.add(Image("", "", "", isAddButton = true))
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("UpdateResidenceActivity", "No images found for this residence")
                    }


                }

                getResidenceViewModel.errorLiveData.observe(this@UpdateResidenceActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(this@UpdateResidenceActivity, errorMessage, Toast.LENGTH_LONG)
                                .show()

                            Log.e("CompleteSignUpActivity", "Complete Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@UpdateResidenceActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun openImagePicker() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                REQUEST_CODE_GALLERY
//            )
//        } else {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            pickImageFromGallery.launch(intent)
//        }

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickImageFromGallery.launch(galleryIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageFromGallery.launch(galleryIntent)
            } else {
                Toast.makeText(this@UpdateResidenceActivity, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImages(imageUrl: String) {
        if (::selectedImage.isInitialized) {
            val file = File(imageUrl)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("images", file.name, requestFile)
            val token = AppReferences.getToken(this@UpdateResidenceActivity)
            val residenceId = intent.getStringExtra("residence_id") ?: ""

            addPhotoResidenceViewModel.uploadResidencePhoto(token, residenceId, body)

            showProgressDialog(this@UpdateResidenceActivity, "Uploading your images...")

            addPhotoResidenceViewModel.uploadPhotoResponseLiveData.observe(this@UpdateResidenceActivity) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status

                    Log.e("status", status)

                }
            }

            addPhotoResidenceViewModel.errorLiveData.observe(this@UpdateResidenceActivity) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(this@UpdateResidenceActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("Upload Image",  error)

                    } catch (_: JSONException) {

                    }
                }
            }
        }
    }

    private fun deleteResidenceImage(imageId: String) {
        val token = AppReferences.getToken(this@UpdateResidenceActivity)
        deleteResidenceImageViewModel.deleteImage(token, imageId)

        showProgressDialog(this@UpdateResidenceActivity, "Deleting your image...")

        deleteResidenceImageViewModel.deleteResidenceImageResponseLiveData.observe(this@UpdateResidenceActivity) { response ->
            hideProgressDialog()
            response?.let {
                val status = it.status

                Log.e("status", status)

            }
        }

        deleteResidenceImageViewModel.errorLiveData.observe(this@UpdateResidenceActivity) { error ->
            hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(this@UpdateResidenceActivity, errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("Delete Image",  error)

                } catch (_: JSONException) {

                }
            }
        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.chip_rent_update, R.id.chip_sell_update,

            R.id.chip_house_update, R.id.chip_apartment_update, R.id.chip_hotel_update,
            R.id.chip_villa_update, R.id.chip_cottage_update,
        )

        chipIds.forEach { chipId ->
            findViewById<Chip>(chipId).setOnCheckedChangeListener { chip, _ ->
                toggleChipSelection(chip as Chip)
            }
        }
    }

    private fun toggleChipSelection(chip: Chip) {
        chip.isSelected = !chip.isSelected
        if (chip.isSelected) {
            chip.setTextColor(Color.WHITE)
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
            Log.e("SelectedChip", "Selected: ${chip.text}")
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
            Log.e("SelectedChip", "Deselected: ${chip.text}")
        }
    }


    private fun setUpActionBar() {
        setSupportActionBar(toolbar_update_residence)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_update_residence.setNavigationOnClickListener {
            onBackPressed()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
//            data?.let {
//                val latitude = it.getDoubleExtra("latitude", 0.0)
//                val longitude = it.getDoubleExtra("longitude", 0.0)
//                val address = it.getStringExtra("address")
//            }
//        }
//    }

}
package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.add_listing.adapter.AddListingPhotosAdapter
import kotlinx.android.synthetic.main.activity_add_listing_photos.button_next_listing_photos
import kotlinx.android.synthetic.main.activity_add_listing_photos.toolbar_add_listing_photos

class AddListingPhotosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddListingPhotosAdapter

    private val imageList = arrayListOf(
        Image("", isAddButton = true)
    )

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val imageUri = data?.data

            if (imageUri != null) {
                addImage(imageUri.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_photos)

        recyclerView = findViewById(R.id.recyclerView_add_listing_photos)

//        val image = ArrayList<Image>()
//
//        image.add(Image("test"))
//
//        image.add(Image("test", true))
//
//        adapter = AddListingPhotosAdapter(image)

        adapter = AddListingPhotosAdapter(imageList) {
            openImagePicker()
        }

        button_next_listing_photos.setOnClickListener {
            startActivity(Intent(this, ExtraInformationActivity::class.java))
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        setUpActionBar()
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun addImage(imagePath: String) {
        adapter.addImage(imagePath)
    }
}
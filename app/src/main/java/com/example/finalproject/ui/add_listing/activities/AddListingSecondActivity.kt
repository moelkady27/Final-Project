package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_listing_second.autoCompleteBuildingType
import kotlinx.android.synthetic.main.activity_add_listing_second.autoCompleteElectrical
import kotlinx.android.synthetic.main.activity_add_listing_second.autoCompleteSaleType
import kotlinx.android.synthetic.main.activity_add_listing_second.btn_next_add_listing_second
import kotlinx.android.synthetic.main.activity_add_listing_second.toolbar_add_listing_second

class AddListingSecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing_second)

        initDropDowns()

        setupActionBar()

        btn_next_add_listing_second.setOnClickListener {
            val intent = Intent(this@AddListingSecondActivity ,
                AddListingThirdActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_listing_second)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_listing_second.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun toggleChipSelection(chip: Chip) {
        chip.isSelected = !chip.isSelected
        if (chip.isSelected) {
            chip.setTextColor(Color.WHITE)
            chip.setChipBackgroundColorResource(R.color.colorPrimary)
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
        }
    }

    private fun initDropDowns() {
        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val arraySaleTypeAdapter = ArrayAdapter(
            this@AddListingSecondActivity, R.layout.dropdown_item, saleTypeItems
        )
        autoCompleteSaleType.setAdapter(arraySaleTypeAdapter)

        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val arrayElectricalAdapter = ArrayAdapter(
            this@AddListingSecondActivity, R.layout.dropdown_item, electricalItems
        )
        autoCompleteElectrical.setAdapter(arrayElectricalAdapter)

        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)
        val arrayBuildingTypeAdapter = ArrayAdapter(
            this@AddListingSecondActivity, R.layout.dropdown_item, buildingTypeItems
        )
        autoCompleteBuildingType.setAdapter(arrayBuildingTypeAdapter)
    }

}
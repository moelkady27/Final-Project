package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteBuildingType
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteElectrical
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteNeighborhood
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteSaleType
import kotlinx.android.synthetic.main.activity_first_complete.btn_next_add_listing_second
import kotlinx.android.synthetic.main.activity_first_complete.toolbar_add_listing_second

class FirstCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_complete)

        val id = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "first is $id")

        initDropDowns()

        setupActionBar()

        btn_next_add_listing_second.setOnClickListener {
            val intent = Intent(this@FirstCompleteActivity ,
                SecondCompleteActivity::class.java)
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
        val neighborhoodItems = resources.getStringArray(R.array.neighborhood_items)
        val arrayNeighborhoodAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, neighborhoodItems
        )
        autoCompleteNeighborhood.setAdapter(arrayNeighborhoodAdapter)

        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val arraySaleTypeAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, saleTypeItems
        )
        autoCompleteSaleType.setAdapter(arraySaleTypeAdapter)

        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val arrayElectricalAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, electricalItems
        )
        autoCompleteElectrical.setAdapter(arrayElectricalAdapter)

        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)
        val arrayBuildingTypeAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, buildingTypeItems
        )
        autoCompleteBuildingType.setAdapter(arrayBuildingTypeAdapter)
    }

}
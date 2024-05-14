package com.example.finalproject.ui.add_listing.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteCondition11
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteCondition22
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteExteriorCovering11
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteExteriorCovering22
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteResidenceType
import kotlinx.android.synthetic.main.activity_second_complete.toolbar_add_listing_third

class SecondCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_complete)

        initDropDowns()

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_listing_third)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_add_listing_third.setNavigationOnClickListener {
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
        val residenceTypeItems = resources.getStringArray(R.array.residence_type_items)
        val arrayResidenceTypeAdapter = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, residenceTypeItems
        )
        autoCompleteResidenceType.setAdapter(arrayResidenceTypeAdapter)

        val exteriorCoveringItems11 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter11 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, exteriorCoveringItems11
        )
        autoCompleteExteriorCovering11.setAdapter(arrayExteriorCoveringAdapter11)

        val exteriorCoveringItems22 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter22 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, exteriorCoveringItems22
        )
        autoCompleteExteriorCovering22.setAdapter(arrayExteriorCoveringAdapter22)

        val conditionsItems11 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter11 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, conditionsItems11
        )
        autoCompleteCondition11.setAdapter(arrayConditionsAdapter11)

        val conditionsItems22 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter22 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, conditionsItems22
        )
        autoCompleteCondition22.setAdapter(arrayConditionsAdapter22)
    }

}
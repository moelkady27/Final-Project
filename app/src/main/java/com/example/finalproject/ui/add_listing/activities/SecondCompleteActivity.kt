package com.example.finalproject.ui.add_listing.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.factory.SecondCompleteFactory
import com.example.finalproject.ui.add_listing.repository.SecondCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.SecondCompleteViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_second_complete.alley_gravel
import kotlinx.android.synthetic.main.activity_second_complete.alley_no_alley_access
import kotlinx.android.synthetic.main.activity_second_complete.alley_paved
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteCondition11
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteCondition22
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteExteriorCovering11
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteExteriorCovering22
import kotlinx.android.synthetic.main.activity_second_complete.autoCompleteResidenceType
import kotlinx.android.synthetic.main.activity_second_complete.btn_next_second_complete
import kotlinx.android.synthetic.main.activity_second_complete.central_air_no
import kotlinx.android.synthetic.main.activity_second_complete.central_air_yes
import kotlinx.android.synthetic.main.activity_second_complete.et_masonry_veneer_area
import kotlinx.android.synthetic.main.activity_second_complete.exterior_condition_average
import kotlinx.android.synthetic.main.activity_second_complete.exterior_condition_excellent
import kotlinx.android.synthetic.main.activity_second_complete.exterior_condition_fair
import kotlinx.android.synthetic.main.activity_second_complete.exterior_condition_good
import kotlinx.android.synthetic.main.activity_second_complete.exterior_condition_poor
import kotlinx.android.synthetic.main.activity_second_complete.exterior_quality_average
import kotlinx.android.synthetic.main.activity_second_complete.exterior_quality_excellent
import kotlinx.android.synthetic.main.activity_second_complete.exterior_quality_fair
import kotlinx.android.synthetic.main.activity_second_complete.exterior_quality_good
import kotlinx.android.synthetic.main.activity_second_complete.exterior_quality_poor
import kotlinx.android.synthetic.main.activity_second_complete.heating_floor
import kotlinx.android.synthetic.main.activity_second_complete.heating_gas
import kotlinx.android.synthetic.main.activity_second_complete.heating_gas_water
import kotlinx.android.synthetic.main.activity_second_complete.heating_gravity
import kotlinx.android.synthetic.main.activity_second_complete.heating_other_water
import kotlinx.android.synthetic.main.activity_second_complete.heating_quality_average
import kotlinx.android.synthetic.main.activity_second_complete.heating_quality_excellent
import kotlinx.android.synthetic.main.activity_second_complete.heating_quality_fair
import kotlinx.android.synthetic.main.activity_second_complete.heating_quality_good
import kotlinx.android.synthetic.main.activity_second_complete.heating_quality_poor
import kotlinx.android.synthetic.main.activity_second_complete.heating_wall
import kotlinx.android.synthetic.main.activity_second_complete.house_style_1Story
import kotlinx.android.synthetic.main.activity_second_complete.house_style_1_5Fin
import kotlinx.android.synthetic.main.activity_second_complete.house_style_1_5Unf
import kotlinx.android.synthetic.main.activity_second_complete.house_style_2Story
import kotlinx.android.synthetic.main.activity_second_complete.house_style_2_5Fin
import kotlinx.android.synthetic.main.activity_second_complete.house_style_2_5Unf
import kotlinx.android.synthetic.main.activity_second_complete.house_style_SFoyer
import kotlinx.android.synthetic.main.activity_second_complete.house_style_SLvl
import kotlinx.android.synthetic.main.activity_second_complete.masonry_veneer_type_brick_common
import kotlinx.android.synthetic.main.activity_second_complete.masonry_veneer_type_brick_face
import kotlinx.android.synthetic.main.activity_second_complete.masonry_veneer_type_cinder_block
import kotlinx.android.synthetic.main.activity_second_complete.masonry_veneer_type_none
import kotlinx.android.synthetic.main.activity_second_complete.masonry_veneer_type_stone
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_clay_or_tile
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_gravel_tar
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_membran
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_metal
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_roll
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_standard_shingle
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_wood_shakes
import kotlinx.android.synthetic.main.activity_second_complete.roof_material_wood_shingles
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_Shed
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_flat
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_gable
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_gambrel
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_hip
import kotlinx.android.synthetic.main.activity_second_complete.roof_style_mansard
import kotlinx.android.synthetic.main.activity_second_complete.street_gravel
import kotlinx.android.synthetic.main.activity_second_complete.street_paved
import kotlinx.android.synthetic.main.activity_second_complete.toolbar_add_listing_third
import org.json.JSONException
import org.json.JSONObject

class SecondCompleteActivity : BaseActivity() {
    private lateinit var networkUtils: NetworkUtils

    private lateinit var secondCompleteViewModel: SecondCompleteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_complete)

        networkUtils = NetworkUtils(this@SecondCompleteActivity)

        initView()

        initDropDowns()

        initChips()

        setupActionBar()
    }

    private fun initView(){
        val secondCompleteRepository = SecondCompleteRepository(RetrofitClient.instance)
        val factory = SecondCompleteFactory(secondCompleteRepository)
        secondCompleteViewModel = ViewModelProvider(
            this@SecondCompleteActivity, factory)[SecondCompleteViewModel::class.java]

        btn_next_second_complete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                secondComplete()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun secondComplete(){
        val roofStyle = when {
            roof_style_flat.isSelected -> "Flat"
            roof_style_gable.isSelected -> "Gable"
            roof_style_gambrel.isSelected -> "Gambrel"
            roof_style_hip.isSelected -> "Hip"
            roof_style_mansard.isSelected -> "Mansard"
            roof_style_Shed.isSelected -> "Shed"
            else -> ""
        }

        val roofMaterial = when {
            roof_material_clay_or_tile.isSelected -> "Clay or Tile"
            roof_material_standard_shingle.isSelected -> "Standard Shingle"
            roof_material_membran.isSelected -> "Membran"
            roof_material_metal.isSelected -> "Metal"
            roof_material_roll.isSelected -> "Roll"
            roof_material_gravel_tar.isSelected -> "Gravel & Tar"
            roof_material_wood_shakes.isSelected -> "Wood Shakes"
            roof_material_wood_shingles.isSelected -> "Wood Shingles"
            else -> ""
        }

        val houseStyle = when {
            house_style_1Story.isSelected -> "1Story"
            house_style_1_5Fin.isSelected -> "1.5Fin"
            house_style_1_5Unf.isSelected -> "1.5Unf"
            house_style_2Story.isSelected -> "2Story"
            house_style_2_5Fin.isSelected -> "2.5Fin"
            house_style_2_5Unf.isSelected -> "2.5Unf"
            house_style_SFoyer.isSelected -> "SFoyer"
            house_style_SLvl.isSelected -> "SLvl"
            else -> ""
        }

        val residenceType = autoCompleteResidenceType.text.toString().trim()

        val centralAir = when {
            central_air_yes.isSelected -> "yes"
            central_air_no.isSelected -> "no"
            else -> ""
        }

        val street = when {
            street_paved.isSelected -> "Paved"
            street_gravel.isSelected -> "Gravel"
            else -> ""
        }

        val alley = when {
            alley_paved.isSelected -> "Paved"
            alley_gravel.isSelected -> "Gravel"
            alley_no_alley_access.isSelected -> "No Alley Access"
            else -> ""
        }

        val heating = when {
            heating_floor.isSelected -> "Floor"
            heating_gas.isSelected -> "Gas"
            heating_gas_water.isSelected -> "Gas Water"
            heating_gravity.isSelected -> "Gravity"
            heating_other_water.isSelected -> "Other Water"
            heating_wall.isSelected -> "Wall"
            else -> ""
        }

        val heatingQuality = when {
            heating_quality_excellent.isSelected -> "Excellent"
            heating_quality_good.isSelected -> "Good"
            heating_quality_average.isSelected -> "Average"
            heating_quality_fair.isSelected -> "Fair"
            heating_quality_poor.isSelected -> "Poor"
            else -> ""
        }

        val masonryVeneerType = when {
            masonry_veneer_type_brick_face.isSelected -> "Brick Face"
            masonry_veneer_type_brick_common.isSelected -> "Brick Common"
            masonry_veneer_type_cinder_block.isSelected -> "Cinder Block"
            masonry_veneer_type_none.isSelected -> "None"
            masonry_veneer_type_stone.isSelected -> "Stone"
            else -> ""
        }

        val masonryVeneerArea = et_masonry_veneer_area.text.toString().trim()

        val exteriorCovering11 = autoCompleteExteriorCovering11.text.toString().trim()
        val exteriorCovering22 = autoCompleteExteriorCovering22.text.toString().trim()

        val exteriorCondition = when {
            exterior_condition_excellent.isSelected -> "Excellent"
            exterior_condition_good.isSelected -> "Good"
            exterior_condition_average.isSelected -> "Average"
            exterior_condition_fair.isSelected -> "Fair"
            exterior_condition_poor.isSelected -> "Poor"
            else -> ""
        }

        val exteriorQuality = when {
            exterior_quality_excellent.isSelected -> "Excellent"
            exterior_quality_good.isSelected -> "Good"
            exterior_quality_average.isSelected -> "Average"
            exterior_quality_fair.isSelected -> "Fair"
            exterior_quality_poor.isSelected -> "Poor"
            else -> ""
        }

        val condition11 = autoCompleteCondition11.text.toString().trim()
        val condition22 = autoCompleteCondition22.text.toString().trim()

        val token = AppReferences.getToken(this@SecondCompleteActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@SecondCompleteActivity, "Please Wait...")
            secondCompleteViewModel.secondComplete(
                token, residenceId, roofStyle, roofMaterial, houseStyle, residenceType,
                centralAir, street, alley, heating, heatingQuality, masonryVeneerType,
                masonryVeneerArea, exteriorCovering11, exteriorCovering22, exteriorCondition,
                exteriorQuality, condition11, condition22
            )

            secondCompleteViewModel.secondCompleteLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("SecondCompleteActivity", "Status: $status")
                }
            }
            secondCompleteViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@SecondCompleteActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("SecondCompleteActivity", "Second Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@SecondCompleteActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val roofStyle = when {
            roof_style_flat.isSelected -> "Flat"
            roof_style_gable.isSelected -> "Gable"
            roof_style_gambrel.isSelected -> "Gambrel"
            roof_style_hip.isSelected -> "Hip"
            roof_style_mansard.isSelected -> "Mansard"
            roof_style_Shed.isSelected -> "Shed"
            else -> ""
        }

        val roofMaterial = when {
            roof_material_clay_or_tile.isSelected -> "Clay or Tile"
            roof_material_standard_shingle.isSelected -> "Standard Shingle"
            roof_material_membran.isSelected -> "Membran"
            roof_material_metal.isSelected -> "Metal"
            roof_material_roll.isSelected -> "Roll"
            roof_material_gravel_tar.isSelected -> "Gravel & Tar"
            roof_material_wood_shakes.isSelected -> "Wood Shakes"
            roof_material_wood_shingles.isSelected -> "Wood Shingles"
            else -> ""
        }

        val houseStyle = when {
            house_style_1Story.isSelected -> "1Story"
            house_style_1_5Fin.isSelected -> "1.5Fin"
            house_style_1_5Unf.isSelected -> "1.5Unf"
            house_style_2Story.isSelected -> "2Story"
            house_style_2_5Fin.isSelected -> "2.5Fin"
            house_style_2_5Unf.isSelected -> "2.5Unf"
            house_style_SFoyer.isSelected -> "SFoyer"
            house_style_SLvl.isSelected -> "SLvl"
            else -> ""
        }

        val residenceType = autoCompleteResidenceType.text.toString().trim()

        val centralAir = when {
            central_air_yes.isSelected -> "yes"
            central_air_no.isSelected -> "no"
            else -> ""
        }

        val street = when {
            street_paved.isSelected -> "Paved"
            street_gravel.isSelected -> "Gravel"
            else -> ""
        }

        val alley = when {
            alley_paved.isSelected -> "Paved"
            alley_gravel.isSelected -> "Gravel"
            alley_no_alley_access.isSelected -> "No Alley Access"
            else -> ""
        }

        val heating = when {
            heating_floor.isSelected -> "Floor"
            heating_gas.isSelected -> "Gas"
            heating_gas_water.isSelected -> "Gas Water"
            heating_gravity.isSelected -> "Gravity"
            heating_other_water.isSelected -> "Other Water"
            heating_wall.isSelected -> "Wall"
            else -> ""
        }

        val heatingQuality = when {
            heating_quality_excellent.isSelected -> "Excellent"
            heating_quality_good.isSelected -> "Good"
            heating_quality_average.isSelected -> "Average"
            heating_quality_fair.isSelected -> "Fair"
            heating_quality_poor.isSelected -> "Poor"
            else -> ""
        }

        val masonryVeneerType = when {
            masonry_veneer_type_brick_face.isSelected -> "Brick Face"
            masonry_veneer_type_brick_common.isSelected -> "Brick Common"
            masonry_veneer_type_cinder_block.isSelected -> "Cinder Block"
            masonry_veneer_type_none.isSelected -> "None"
            masonry_veneer_type_stone.isSelected -> "Stone"
            else -> ""
        }

        val masonryVeneerArea = et_masonry_veneer_area.text.toString().trim()

        val exteriorCovering11 = autoCompleteExteriorCovering11.text.toString().trim()
        val exteriorCovering22 = autoCompleteExteriorCovering22.text.toString().trim()

        val exteriorCondition = when {
            exterior_condition_excellent.isSelected -> "Excellent"
            exterior_condition_good.isSelected -> "Good"
            exterior_condition_average.isSelected -> "Average"
            exterior_condition_fair.isSelected -> "Fair"
            exterior_condition_poor.isSelected -> "Poor"
            else -> ""
        }

        val exteriorQuality = when {
            exterior_quality_excellent.isSelected -> "Excellent"
            exterior_quality_good.isSelected -> "Good"
            exterior_quality_average.isSelected -> "Average"
            exterior_quality_fair.isSelected -> "Fair"
            exterior_quality_poor.isSelected -> "Poor"
            else -> ""
        }

        val condition11 = autoCompleteCondition11.text.toString().trim()
        val condition22 = autoCompleteCondition22.text.toString().trim()

        val residenceTypeItems = resources.getStringArray(R.array.residence_type_items)
        val exteriorCoveringItems = resources.getStringArray(R.array.exterior_covering_items)
        val conditionsItems = resources.getStringArray(R.array.conditions_items)

        if (roofStyle.isEmpty()) {
            showErrorSnackBarResidence("Please Select Roof Style", true)
            return false
        }

        if (roofMaterial.isEmpty()) {
            showErrorSnackBarResidence("Please Select Roof Material", true)
            return false
        }

        if (houseStyle.isEmpty()) {
            showErrorSnackBarResidence("Please Select House Style", true)
            return false
        }

        if (residenceType.isEmpty()) {
            autoCompleteResidenceType.error = "Residence Type is not allowed to be empty."
            return false
        }

        if (centralAir.isEmpty()) {
            showErrorSnackBarResidence("Central Air must be No or Yes.", true)
            return false
        }

        if (street.isEmpty()) {
            showErrorSnackBarResidence("Street must be Gravel or Paved.", true)
            return false
        }

        if (alley.isEmpty()) {
            showErrorSnackBarResidence(
                "Alley access must be Gravel, Paved or No Alley Access.", true)
            return false
        }

        if (heating.isEmpty()) {
            showErrorSnackBarResidence("Please Select Heating", true)
            return false
        }

        if (heatingQuality.isEmpty()) {
            showErrorSnackBarResidence("Please Select Heating Quality", true)
            return false
        }

        if (masonryVeneerType.isEmpty()) {
            showErrorSnackBarResidence("Please Select Masonry Veneer Type", true)
            return false
        }

        if (masonryVeneerArea.isEmpty()) {
            et_masonry_veneer_area.error = "Masonry Veneer Area is not allowed to be empty."
            return false
        }

        if (exteriorCovering11.isEmpty()) {
            autoCompleteExteriorCovering11.error = "Exterior Covering is not allowed to be empty."
            return false
        }

        if (exteriorCovering22.isEmpty()) {
            autoCompleteExteriorCovering22.error = "Exterior Covering is not allowed to be empty."
            return false
        }

        if (exteriorCondition.isEmpty()) {
            showErrorSnackBarResidence("Please Select Exterior Condition", true)
            return false
        }

        if (exteriorQuality.isEmpty()) {
            showErrorSnackBarResidence("Please Select Exterior Quality", true)
            return false
        }

        if (condition11.isEmpty()) {
            autoCompleteCondition11.error = "Condition is not allowed to be empty."
            return false
        }

        if (condition22.isEmpty()) {
            autoCompleteCondition22.error = "Condition is not allowed to be empty."
            return false
        }

        if (!residenceTypeItems.contains(autoCompleteResidenceType.text.toString().trim())) {
            autoCompleteResidenceType.error = "Please select a valid residence type."
            return false
        }

        if (!exteriorCoveringItems.contains(autoCompleteExteriorCovering11.text.toString().trim())) {
            autoCompleteExteriorCovering11.error = "Please select a valid exterior covering."
            return false
        }

        if (!exteriorCoveringItems.contains(autoCompleteExteriorCovering22.text.toString().trim())) {
            autoCompleteExteriorCovering22.error = "Please select a valid exterior covering."
            return false
        }

        if (!conditionsItems.contains(autoCompleteCondition11.text.toString().trim())) {
            autoCompleteCondition11.error = "Please select a valid condition."
            return false
        }

        if (!conditionsItems.contains(autoCompleteCondition22.text.toString().trim())) {
            autoCompleteCondition22.error = "Please select a valid condition."
            return false
        }

        return true
    }

    private fun initDropDowns() {
        val residenceTypeItems = resources.getStringArray(R.array.residence_type_items)
        val arrayResidenceTypeAdapter = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, residenceTypeItems
        )
        autoCompleteResidenceType.setAdapter(arrayResidenceTypeAdapter)
        autoCompleteResidenceType.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedResidenceType", "Selected Residence Type: $selectedItem")
        }

        val exteriorCoveringItems11 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter11 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, exteriorCoveringItems11
        )
        autoCompleteExteriorCovering11.setAdapter(arrayExteriorCoveringAdapter11)
        autoCompleteExteriorCovering11.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedExteriorCovering11", "Selected Exterior Covering 1: $selectedItem")
        }

        val exteriorCoveringItems22 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter22 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, exteriorCoveringItems22
        )
        autoCompleteExteriorCovering22.setAdapter(arrayExteriorCoveringAdapter22)
        autoCompleteExteriorCovering22.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedExteriorCovering22", "Selected Exterior Covering 2: $selectedItem")
        }

        val conditionsItems11 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter11 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, conditionsItems11
        )
        autoCompleteCondition11.setAdapter(arrayConditionsAdapter11)
        autoCompleteCondition11.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedCondition11", "Selected Condition 1: $selectedItem")
        }

        val conditionsItems22 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter22 = ArrayAdapter(
            this@SecondCompleteActivity, R.layout.dropdown_item, conditionsItems22
        )
        autoCompleteCondition22.setAdapter(arrayConditionsAdapter22)
        autoCompleteCondition22.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedCondition22", "Selected Condition 2: $selectedItem")
        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.roof_style_flat, R.id.roof_style_gable,
            R.id.roof_style_gambrel, R.id.roof_style_hip,
            R.id.roof_style_mansard, R.id.roof_style_Shed,

            R.id.roof_material_clay_or_tile, R.id.roof_material_standard_shingle,
            R.id.roof_material_membran, R.id.roof_material_metal,
            R.id.roof_material_roll, R.id.roof_material_gravel_tar,
            R.id.roof_material_wood_shakes, R.id.roof_material_wood_shingles,

            R.id.house_style_1Story, R.id.house_style_1_5Fin,
            R.id.house_style_1_5Unf, R.id.house_style_2Story,
            R.id.house_style_2_5Fin, R.id.house_style_2_5Unf,
            R.id.house_style_SFoyer, R.id.house_style_SLvl,

            R.id.central_air_yes, R.id.central_air_no,

            R.id.street_paved, R.id.street_gravel,

            R.id.alley_paved, R.id.alley_gravel, R.id.alley_no_alley_access,

            R.id.heating_floor, R.id.heating_gas,
            R.id.heating_gas_water, R.id.heating_gravity,
            R.id.heating_other_water, R.id.heating_wall,

            R.id.heating_quality_excellent, R.id.heating_quality_good,
            R.id.heating_quality_average, R.id.heating_quality_fair, R.id.heating_quality_poor,

            R.id.masonry_veneer_type_brick_face,
            R.id.masonry_veneer_type_brick_common, R.id.masonry_veneer_type_cinder_block,
            R.id.masonry_veneer_type_none, R.id.masonry_veneer_type_stone,

            R.id.exterior_condition_excellent, R.id.exterior_condition_good,
            R.id.exterior_condition_average, R.id.exterior_condition_fair, R.id.exterior_condition_poor,

            R.id.exterior_quality_excellent, R.id.exterior_quality_good,
            R.id.exterior_quality_average, R.id.exterior_quality_fair, R.id.exterior_quality_poor
        )

        chipIds.forEach { chipId ->
            findViewById<Chip>(chipId).setOnCheckedChangeListener { chip, _ ->
                toggleChipSelection(chip as Chip)
            }
        }
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

}
package com.example.finalproject.ui.update_listing.activities

import android.content.Intent
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
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.factory.SecondUpdateFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.repository.SecondUpdateRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.example.finalproject.ui.update_listing.viewModel.SecondUpdateViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_second_update.alley_gravel_update
import kotlinx.android.synthetic.main.activity_second_update.alley_no_alley_access_update
import kotlinx.android.synthetic.main.activity_second_update.alley_paved_update
import kotlinx.android.synthetic.main.activity_second_update.auto_complete_condition_update11
import kotlinx.android.synthetic.main.activity_second_update.auto_complete_condition_update22
import kotlinx.android.synthetic.main.activity_second_update.auto_complete_exterior_covering_update11
import kotlinx.android.synthetic.main.activity_second_update.auto_complete_exterior_covering_update22
import kotlinx.android.synthetic.main.activity_second_update.auto_complete_residence_type_update
import kotlinx.android.synthetic.main.activity_second_update.btn_next_second_update
import kotlinx.android.synthetic.main.activity_second_update.central_air_no_update
import kotlinx.android.synthetic.main.activity_second_update.central_air_yes_update
import kotlinx.android.synthetic.main.activity_second_update.et_masonry_veneer_area_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_condition_average_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_condition_excellent_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_condition_fair_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_condition_good_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_condition_poor_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_quality_average_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_quality_excellent_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_quality_fair_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_quality_good_update
import kotlinx.android.synthetic.main.activity_second_update.exterior_quality_poor_update
import kotlinx.android.synthetic.main.activity_second_update.heating_floor_update
import kotlinx.android.synthetic.main.activity_second_update.heating_gas_update
import kotlinx.android.synthetic.main.activity_second_update.heating_gas_water_update
import kotlinx.android.synthetic.main.activity_second_update.heating_gravity_update
import kotlinx.android.synthetic.main.activity_second_update.heating_other_water_update
import kotlinx.android.synthetic.main.activity_second_update.heating_quality_average_update
import kotlinx.android.synthetic.main.activity_second_update.heating_quality_excellent_update
import kotlinx.android.synthetic.main.activity_second_update.heating_quality_fair_update
import kotlinx.android.synthetic.main.activity_second_update.heating_quality_good_update
import kotlinx.android.synthetic.main.activity_second_update.heating_quality_poor_update
import kotlinx.android.synthetic.main.activity_second_update.heating_wall_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_1Story_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_1_5Fin_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_1_5Unf_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_2Story_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_2_5Fin_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_2_5Unf_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_SFoyer_update
import kotlinx.android.synthetic.main.activity_second_update.house_style_SLvl_update
import kotlinx.android.synthetic.main.activity_second_update.masonry_veneer_type_brick_common_update
import kotlinx.android.synthetic.main.activity_second_update.masonry_veneer_type_brick_face_update
import kotlinx.android.synthetic.main.activity_second_update.masonry_veneer_type_cinder_block_update
import kotlinx.android.synthetic.main.activity_second_update.masonry_veneer_type_none_update
import kotlinx.android.synthetic.main.activity_second_update.masonry_veneer_type_stone_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_clay_or_tile_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_gravel_tar_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_membran_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_metal_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_roll_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_standard_shingle_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_wood_shakes_update
import kotlinx.android.synthetic.main.activity_second_update.roof_material_wood_shingles_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_Shed_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_flat_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_gable_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_gambrel_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_hip_update
import kotlinx.android.synthetic.main.activity_second_update.roof_style_mansard_update
import kotlinx.android.synthetic.main.activity_second_update.street_gravel_update
import kotlinx.android.synthetic.main.activity_second_update.street_paved_update
import kotlinx.android.synthetic.main.activity_second_update.toolbar_second_update
import org.json.JSONException
import org.json.JSONObject

class SecondUpdateActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var secondUpdateViewModel: SecondUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_update)

        networkUtils = NetworkUtils(this@SecondUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("Residence ID", residenceId.toString())

        initChips()

        initDropDowns()

        initView()

        setUpActionBar()
    }

    private fun initView() {
                                    /* Get-Residence */

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@SecondUpdateActivity, factoryGetResidence)[GetResidenceViewModel::class.java]

        getResidence()

                                /* Update-Second-Residence */

        val secondUpdateRepository = SecondUpdateRepository(RetrofitClient.instance)
        val factorySecondUpdate = SecondUpdateFactory(secondUpdateRepository)
        secondUpdateViewModel = ViewModelProvider(
            this@SecondUpdateActivity, factorySecondUpdate)[SecondUpdateViewModel::class.java]

        btn_next_second_update.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                secondUpdate()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@SecondUpdateActivity)
            val residenceId = intent.getStringExtra("residenceId").toString()

            Log.e("Residence ID", "ResidenceID: $residenceId")

            showProgressDialog(this@SecondUpdateActivity, "please wait...")
            getResidenceViewModel.getResidence(token, residenceId)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@SecondUpdateActivity) { response ->
                hideProgressDialog()
                response.let {
                    when (response.residence.roofStyle) {
                        "Flat" -> roof_style_flat_update.isChecked = true
                        "Gable" -> roof_style_gable_update.isChecked = true
                        "Gambrel" -> roof_style_gambrel_update.isChecked = true
                        "Hip" -> roof_style_hip_update.isChecked = true
                        "Mansard" -> roof_style_mansard_update.isChecked = true
                        "Shed" -> roof_style_Shed_update.isChecked = true
                    }

                    when (response.residence.roofMatl) {
                        "clay or tile" -> roof_material_clay_or_tile_update.isChecked = true
                        "standard shingle" -> roof_material_standard_shingle_update.isChecked = true
                        "membran" -> roof_material_membran_update.isChecked = true
                        "metal" -> roof_material_metal_update.isChecked = true
                        "roll" -> roof_material_roll_update.isChecked = true
                        "gravel & tar" -> roof_material_gravel_tar_update.isChecked = true
                        "wood shakes" -> roof_material_wood_shakes_update.isChecked = true
                        "wood shingles" -> roof_material_wood_shingles_update.isChecked = true
                    }

                    when (response.residence.houseStyle) {
                        "1Story" -> house_style_1Story_update.isChecked = true
                        "1.5Fin" -> house_style_1_5Fin_update.isChecked = true
                        "1.5Unf" -> house_style_1_5Unf_update.isChecked = true
                        "2Story" -> house_style_2Story_update.isChecked = true
                        "2.5Fin" -> house_style_2_5Fin_update.isChecked = true
                        "2.5Unf" -> house_style_2_5Unf_update.isChecked = true
                        "SFoyer" -> house_style_SFoyer_update.isChecked = true
                        "SLvl" -> house_style_SLvl_update.isChecked = true
                    }

                    auto_complete_residence_type_update.setText(response.residence.msSubClass)

                    when (response.residence.centralAir) {
                        "yes" -> {
                            central_air_yes_update.isChecked = true
                            central_air_no_update.isChecked = false
                        }

                        "no" -> {
                            central_air_no_update.isChecked = true
                            central_air_yes_update.isChecked = false
                        }
                    }

                    when (response.residence.street) {
                        "paved" -> {
                            street_paved_update.isChecked = true
                            street_gravel_update.isChecked = false
                        }

                        "gravel" -> {
                            street_gravel_update.isChecked = true
                            street_paved_update.isChecked = false
                        }
                    }

                    when (response.residence.alley) {
                        "paved" -> alley_paved_update.isChecked = true
                        "gravel" -> alley_gravel_update.isChecked = true
                        "no alley access" -> alley_no_alley_access_update.isChecked = true
                    }

                    when (response.residence.heating) {
                        "floor" -> heating_floor_update.isChecked = true
                        "gas" -> heating_gas_update.isChecked = true
                        "gas water" -> heating_gas_water_update.isChecked = true
                        "gravity" -> heating_gravity_update.isChecked = true
                        "other water" -> heating_other_water_update.isChecked = true
                        "wall" -> heating_wall_update.isChecked = true
                    }

                    when (response.residence.heatingQc) {
                        "excellent" -> heating_quality_excellent_update.isChecked = true
                        "good" -> heating_quality_good_update.isChecked = true
                        "average" -> heating_quality_average_update.isChecked = true
                        "fair" -> heating_quality_fair_update.isChecked = true
                        "poor" -> heating_quality_poor_update.isChecked = true
                    }

                    when (response.residence.masVnrType) {
                        "brick face" -> masonry_veneer_type_brick_face_update.isChecked = true
                        "brick common" -> masonry_veneer_type_brick_common_update.isChecked = true
                        "cinder block" -> masonry_veneer_type_cinder_block_update.isChecked = true
                        "none" -> masonry_veneer_type_none_update.isChecked = true
                        "stone" -> masonry_veneer_type_stone_update.isChecked = true
                    }

                    et_masonry_veneer_area_update.setText(response.residence.masVnrArea.toString())

                    auto_complete_exterior_covering_update11.setText(response.residence.exterior1st)

                    auto_complete_exterior_covering_update22.setText(response.residence.exterior2nd)

                    when (response.residence.exterCond) {
                        "excellent" -> exterior_condition_excellent_update.isChecked = true
                        "good" -> exterior_condition_good_update.isChecked = true
                        "average" -> exterior_condition_average_update.isChecked = true
                        "fair" -> exterior_condition_fair_update.isChecked = true
                        "poor" -> exterior_condition_poor_update.isChecked = true
                    }

                    when (response.residence.exterQual) {
                        "excellent" -> exterior_quality_excellent_update.isChecked = true
                        "good" -> exterior_quality_good_update.isChecked = true
                        "average" -> exterior_quality_average_update.isChecked = true
                        "fair" -> exterior_quality_fair_update.isChecked = true
                        "poor" -> exterior_quality_poor_update.isChecked = true
                    }

                    auto_complete_condition_update11.setText(response.residence.condition1)

                    auto_complete_condition_update22.setText(response.residence.condition2)
                }

                getResidenceViewModel.errorLiveData.observe(this@SecondUpdateActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(
                                this@SecondUpdateActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e("SecondUpdateActivity", "Second Update Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@SecondUpdateActivity, error, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
        else {
            showErrorSnackBar("No internet connection", true)
        }
    }

    private fun secondUpdate() {
        val roofStyle = when {
            roof_style_flat_update.isSelected -> "Flat"
            roof_style_gable_update.isSelected -> "Gable"
            roof_style_gambrel_update.isSelected -> "Gambrel"
            roof_style_hip_update.isSelected -> "Hip"
            roof_style_mansard_update.isSelected -> "Mansard"
            roof_style_Shed_update.isSelected -> "Shed"
            else -> ""
        }

        val roofMaterial = when {
            roof_material_clay_or_tile_update.isSelected -> "clay or tile"
            roof_material_standard_shingle_update.isSelected -> "standard shingle"
            roof_material_membran_update.isSelected -> "membran"
            roof_material_metal_update.isSelected -> "metal"
            roof_material_roll_update.isSelected -> "roll"
            roof_material_gravel_tar_update.isSelected -> "gravel & tar"
            roof_material_wood_shakes_update.isSelected -> "wood shakes"
            roof_material_wood_shingles_update.isSelected -> "wood shingles"
            else -> ""
        }

        val houseStyle = when {
            house_style_1Story_update.isSelected -> "1Story"
            house_style_1_5Fin_update.isSelected -> "1.5Fin"
            house_style_1_5Unf_update.isSelected -> "1.5Unf"
            house_style_2Story_update.isSelected -> "2Story"
            house_style_2_5Fin_update.isSelected -> "2.5Fin"
            house_style_2_5Unf_update.isSelected -> "2.5Unf"
            house_style_SFoyer_update.isSelected -> "SFoyer"
            house_style_SLvl_update.isSelected -> "SLvl"
            else -> ""
        }

        val residenceType = auto_complete_residence_type_update.text.toString().trim()

        val centralAir = when {
            central_air_yes_update.isSelected -> "yes"
            central_air_no_update.isSelected -> "no"
            else -> ""
        }

        val street = when {
            street_paved_update.isSelected -> "paved"
            street_gravel_update.isSelected -> "gravel"
            else -> ""
        }

        val alley = when {
            alley_paved_update.isSelected -> "paved"
            alley_gravel_update.isSelected -> "gravel"
            alley_no_alley_access_update.isSelected -> "no alley access"
            else -> ""
        }

        val heating = when {
            heating_floor_update.isSelected -> "floor"
            heating_gas_update.isSelected -> "gas"
            heating_gas_water_update.isSelected -> "gas water"
            heating_gravity_update.isSelected -> "gravity"
            heating_other_water_update.isSelected -> "other water"
            heating_wall_update.isSelected -> "wall"
            else -> ""
        }

        val heatingQuality = when {
            heating_quality_excellent_update.isSelected -> "excellent"
            heating_quality_good_update.isSelected -> "good"
            heating_quality_average_update.isSelected -> "average"
            heating_quality_fair_update.isSelected -> "fair"
            heating_quality_poor_update.isSelected -> "poor"
            else -> ""
        }

        val masonryVeneerType = when {
            masonry_veneer_type_brick_face_update.isSelected -> "brick face"
            masonry_veneer_type_brick_common_update.isSelected -> "brick common"
            masonry_veneer_type_cinder_block_update.isSelected -> "cinder block"
            masonry_veneer_type_none_update.isSelected -> "none"
            masonry_veneer_type_stone_update.isSelected -> "stone"
            else -> ""
        }

        val masonryVeneerArea = et_masonry_veneer_area_update.text.toString().trim()

        val exteriorCovering11 = auto_complete_exterior_covering_update11.text.toString().trim()
        val exteriorCovering22 = auto_complete_exterior_covering_update22.text.toString().trim()

        val exteriorCondition = when {
            exterior_condition_excellent_update.isSelected -> "excellent"
            exterior_condition_good_update.isSelected -> "good"
            exterior_condition_average_update.isSelected -> "average"
            exterior_condition_fair_update.isSelected -> "fair"
            exterior_condition_poor_update.isSelected -> "poor"
            else -> ""
        }

        val exteriorQuality = when {
            exterior_quality_excellent_update.isSelected -> "excellent"
            exterior_quality_good_update.isSelected -> "good"
            exterior_quality_average_update.isSelected -> "average"
            exterior_quality_fair_update.isSelected -> "fair"
            exterior_quality_poor_update.isSelected -> "poor"
            else -> ""
        }

        val condition11 = auto_complete_condition_update11.text.toString().trim()
        val condition22 = auto_complete_condition_update22.text.toString().trim()

        val token = AppReferences.getToken(this@SecondUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@SecondUpdateActivity, "Please Wait...")
            secondUpdateViewModel.secondUpdate(
                token, residenceId, roofStyle, roofMaterial, houseStyle, residenceType,
                centralAir, street, alley, heating, heatingQuality, masonryVeneerType,
                masonryVeneerArea, exteriorCovering11, exteriorCovering22, exteriorCondition,
                exteriorQuality, condition11, condition22
            )

            secondUpdateViewModel.secondUpdateLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("SecondUpdateActivity", "Status: $status")

                    val intent = Intent(
                        this@SecondUpdateActivity, ThirdUpdateActivity::class.java)
                    intent.putExtra("residenceId", residenceId)
                    Log.e("residenceId" , "Second Update is $residenceId")
                    startActivity(intent)
                }
            }
            secondUpdateViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@SecondUpdateActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("SecondUpdateActivity", "Second Update Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@SecondUpdateActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val roofStyle = when {
            roof_style_flat_update.isSelected -> "Flat"
            roof_style_gable_update.isSelected -> "Gable"
            roof_style_gambrel_update.isSelected -> "Gambrel"
            roof_style_hip_update.isSelected -> "Hip"
            roof_style_mansard_update.isSelected -> "Mansard"
            roof_style_Shed_update.isSelected -> "Shed"
            else -> ""
        }

        val roofMaterial = when {
            roof_material_clay_or_tile_update.isSelected -> "clay or tile"
            roof_material_standard_shingle_update.isSelected -> "standard shingle"
            roof_material_membran_update.isSelected -> "membran"
            roof_material_metal_update.isSelected -> "metal"
            roof_material_roll_update.isSelected -> "roll"
            roof_material_gravel_tar_update.isSelected -> "gravel & tar"
            roof_material_wood_shakes_update.isSelected -> "wood shakes"
            roof_material_wood_shingles_update.isSelected -> "wood shingles"
            else -> ""
        }

        val houseStyle = when {
            house_style_1Story_update.isSelected -> "1Story"
            house_style_1_5Fin_update.isSelected -> "1.5Fin"
            house_style_1_5Unf_update.isSelected -> "1.5Unf"
            house_style_2Story_update.isSelected -> "2Story"
            house_style_2_5Fin_update.isSelected -> "2.5Fin"
            house_style_2_5Unf_update.isSelected -> "2.5Unf"
            house_style_SFoyer_update.isSelected -> "SFoyer"
            house_style_SLvl_update.isSelected -> "SLvl"
            else -> ""
        }

        val residenceType = auto_complete_residence_type_update.text.toString().trim()

        val centralAir = when {
            central_air_yes_update.isSelected -> "yes"
            central_air_no_update.isSelected -> "no"
            else -> ""
        }

        val street = when {
            street_paved_update.isSelected -> "paved"
            street_gravel_update.isSelected -> "gravel"
            else -> ""
        }

        val alley = when {
            alley_paved_update.isSelected -> "paved"
            alley_gravel_update.isSelected -> "gravel"
            alley_no_alley_access_update.isSelected -> "no alley access"
            else -> ""
        }

        val heating = when {
            heating_floor_update.isSelected -> "floor"
            heating_gas_update.isSelected -> "gas"
            heating_gas_water_update.isSelected -> "gas water"
            heating_gravity_update.isSelected -> "gravity"
            heating_other_water_update.isSelected -> "other water"
            heating_wall_update.isSelected -> "wall"
            else -> ""
        }

        val heatingQuality = when {
            heating_quality_excellent_update.isSelected -> "excellent"
            heating_quality_good_update.isSelected -> "good"
            heating_quality_average_update.isSelected -> "average"
            heating_quality_fair_update.isSelected -> "fair"
            heating_quality_poor_update.isSelected -> "poor"
            else -> ""
        }

        val masonryVeneerType = when {
            masonry_veneer_type_brick_face_update.isSelected -> "brick face"
            masonry_veneer_type_brick_common_update.isSelected -> "brick common"
            masonry_veneer_type_cinder_block_update.isSelected -> "cinder block"
            masonry_veneer_type_none_update.isSelected -> "none"
            masonry_veneer_type_stone_update.isSelected -> "stone"
            else -> ""
        }

        val masonryVeneerArea = et_masonry_veneer_area_update.text.toString().trim()

        val exteriorCovering11 = auto_complete_exterior_covering_update11.text.toString().trim()
        val exteriorCovering22 = auto_complete_exterior_covering_update22.text.toString().trim()

        val exteriorCondition = when {
            exterior_condition_excellent_update.isSelected -> "excellent"
            exterior_condition_good_update.isSelected -> "good"
            exterior_condition_average_update.isSelected -> "average"
            exterior_condition_fair_update.isSelected -> "fair"
            exterior_condition_poor_update.isSelected -> "poor"
            else -> ""
        }

        val exteriorQuality = when {
            exterior_quality_excellent_update.isSelected -> "excellent"
            exterior_quality_good_update.isSelected -> "good"
            exterior_quality_average_update.isSelected -> "average"
            exterior_quality_fair_update.isSelected -> "fair"
            exterior_quality_poor_update.isSelected -> "poor"
            else -> ""
        }

        val condition11 = auto_complete_condition_update11.text.toString().trim()
        val condition22 = auto_complete_condition_update22.text.toString().trim()

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
            auto_complete_residence_type_update.error = "Residence Type is not allowed to be empty."
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
            et_masonry_veneer_area_update.error = "Masonry Veneer Area is not allowed to be empty."
            return false
        }

        if (exteriorCovering11.isEmpty()) {
            auto_complete_exterior_covering_update11.error = "Exterior Covering is not allowed to be empty."
            return false
        }

        if (exteriorCovering22.isEmpty()) {
            auto_complete_exterior_covering_update22.error = "Exterior Covering is not allowed to be empty."
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
            auto_complete_condition_update11.error = "Condition is not allowed to be empty."
            return false
        }

        if (condition22.isEmpty()) {
            auto_complete_condition_update22.error = "Condition is not allowed to be empty."
            return false
        }

        if (!residenceTypeItems.any {
            it.equals(auto_complete_residence_type_update.text.toString().trim(), ignoreCase = true) }) {
            auto_complete_residence_type_update.error = "Please select a valid residence type."
            return false
        }

        if (!exteriorCoveringItems.any {
            it.equals(
                auto_complete_exterior_covering_update11.text.toString().trim(), ignoreCase = true)
        }) {
            auto_complete_exterior_covering_update11.error = "Please select a valid exterior covering."
            return false
        }

        if (!exteriorCoveringItems.any {
                it.equals(
                    auto_complete_exterior_covering_update22.text.toString().trim(), ignoreCase = true)
            }) {
            auto_complete_exterior_covering_update22.error = "Please select a valid exterior covering."
            return false
        }

        if (!conditionsItems.any {
                it.equals(
                    auto_complete_condition_update11.text.toString().trim(), ignoreCase = true)
            }) {
            auto_complete_condition_update11.error = "Please select a valid condition."
            return false
        }

        if (!conditionsItems.any {
                it.equals(
                    auto_complete_condition_update22.text.toString().trim(), ignoreCase = true)
            }) {
            auto_complete_condition_update22.error = "Please select a valid condition."
            return false
        }

        return true
    }

    private fun initDropDowns() {
        val residenceTypeItems = resources.getStringArray(R.array.residence_type_items)
        val arrayResidenceTypeAdapter = ArrayAdapter(
            this@SecondUpdateActivity, R.layout.dropdown_item, residenceTypeItems
        )
        auto_complete_residence_type_update.setAdapter(arrayResidenceTypeAdapter)
        auto_complete_residence_type_update.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedResidenceTypeUpdate", "Selected Residence Type Update: $selectedItem")
        }

        val exteriorCoveringItems11 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter11 = ArrayAdapter(
            this@SecondUpdateActivity, R.layout.dropdown_item, exteriorCoveringItems11
        )
        auto_complete_exterior_covering_update11.setAdapter(arrayExteriorCoveringAdapter11)
        auto_complete_exterior_covering_update11.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedExteriorCoveringUpdate11", "Selected Exterior Covering Update 1: $selectedItem")
        }

        val exteriorCoveringItems22 = resources.getStringArray(R.array.exterior_covering_items)
        val arrayExteriorCoveringAdapter22 = ArrayAdapter(
            this@SecondUpdateActivity, R.layout.dropdown_item, exteriorCoveringItems22
        )
        auto_complete_exterior_covering_update22.setAdapter(arrayExteriorCoveringAdapter22)
        auto_complete_exterior_covering_update22.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedExteriorCoveringUpdate22", "Selected Exterior Covering Update 2: $selectedItem")
        }

        val conditionsItems11 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter11 = ArrayAdapter(
            this@SecondUpdateActivity, R.layout.dropdown_item, conditionsItems11
        )
        auto_complete_condition_update11.setAdapter(arrayConditionsAdapter11)
        auto_complete_condition_update11.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedConditionUpdate11", "Selected Condition Update 1: $selectedItem")
        }

        val conditionsItems22 = resources.getStringArray(R.array.conditions_items)
        val arrayConditionsAdapter22 = ArrayAdapter(
            this@SecondUpdateActivity, R.layout.dropdown_item, conditionsItems22
        )
        auto_complete_condition_update22.setAdapter(arrayConditionsAdapter22)
        auto_complete_condition_update22.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedConditionUpdate22", "Selected Condition Update 2: $selectedItem")
        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.roof_style_flat_update, R.id.roof_style_gable_update,
            R.id.roof_style_gambrel_update, R.id.roof_style_hip_update,
            R.id.roof_style_mansard_update, R.id.roof_style_Shed_update,

            R.id.roof_material_clay_or_tile_update, R.id.roof_material_standard_shingle_update,
            R.id.roof_material_membran_update, R.id.roof_material_metal_update,
            R.id.roof_material_roll_update, R.id.roof_material_gravel_tar_update,
            R.id.roof_material_wood_shakes_update, R.id.roof_material_wood_shingles_update,

            R.id.house_style_1Story_update, R.id.house_style_1_5Fin_update,
            R.id.house_style_1_5Unf_update, R.id.house_style_2Story_update,
            R.id.house_style_2_5Fin_update, R.id.house_style_2_5Unf_update,
            R.id.house_style_SFoyer_update, R.id.house_style_SLvl_update,

            R.id.central_air_yes_update, R.id.central_air_no_update,

            R.id.street_paved_update, R.id.street_gravel_update,

            R.id.alley_paved_update, R.id.alley_gravel_update, R.id.alley_no_alley_access_update,

            R.id.heating_floor_update, R.id.heating_gas_update,
            R.id.heating_gas_water_update, R.id.heating_gravity_update,
            R.id.heating_other_water_update, R.id.heating_wall_update,

            R.id.heating_quality_excellent_update, R.id.heating_quality_good_update,
            R.id.heating_quality_average_update, R.id.heating_quality_fair_update,
            R.id.heating_quality_poor_update,

            R.id.masonry_veneer_type_brick_face_update,
            R.id.masonry_veneer_type_brick_common_update, R.id.masonry_veneer_type_cinder_block_update,
            R.id.masonry_veneer_type_none_update, R.id.masonry_veneer_type_stone_update,

            R.id.exterior_condition_excellent_update, R.id.exterior_condition_good_update,
            R.id.exterior_condition_average_update, R.id.exterior_condition_fair_update,
            R.id.exterior_condition_poor_update,

            R.id.exterior_quality_excellent_update, R.id.exterior_quality_good_update,
            R.id.exterior_quality_average_update, R.id.exterior_quality_fair_update,
            R.id.exterior_quality_poor_update
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
        } else {
            chip.setTextColor(resources.getColor(R.color.colorPrimaryText))
            chip.setChipBackgroundColorResource(R.color.home_search)
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_second_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_second_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
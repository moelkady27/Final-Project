package com.example.finalproject.ui.add_listing.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.factory.FourthCompleteFactory
import com.example.finalproject.ui.add_listing.repository.FourthCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.FourthCompleteViewModel
import com.example.finalproject.ui.home.fragment.HomeFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_fourth_complete.btn_submit_fourth_complete
import kotlinx.android.synthetic.main.activity_fourth_complete.et_house_age
import kotlinx.android.synthetic.main.activity_fourth_complete.et_house_remodel_age
import kotlinx.android.synthetic.main.activity_fourth_complete.et_lot_area
import kotlinx.android.synthetic.main.activity_fourth_complete.et_lot_frontage
import kotlinx.android.synthetic.main.activity_fourth_complete.et_low_quality_finished_sf
import kotlinx.android.synthetic.main.activity_fourth_complete.et_misc_value
import kotlinx.android.synthetic.main.activity_fourth_complete.et_overall_condition
import kotlinx.android.synthetic.main.activity_fourth_complete.et_overall_quality
import kotlinx.android.synthetic.main.activity_fourth_complete.et_pool_area
import kotlinx.android.synthetic.main.activity_fourth_complete.et_total_area
import kotlinx.android.synthetic.main.activity_fourth_complete.et_total_porch_sf
import kotlinx.android.synthetic.main.activity_fourth_complete.et_total_square_feet
import kotlinx.android.synthetic.main.activity_fourth_complete.landContour_banked
import kotlinx.android.synthetic.main.activity_fourth_complete.landContour_depression
import kotlinx.android.synthetic.main.activity_fourth_complete.landContour_hillside
import kotlinx.android.synthetic.main.activity_fourth_complete.landContour_level
import kotlinx.android.synthetic.main.activity_fourth_complete.landSlope_gentle
import kotlinx.android.synthetic.main.activity_fourth_complete.landSlope_moderate
import kotlinx.android.synthetic.main.activity_fourth_complete.landSlope_severe
import kotlinx.android.synthetic.main.activity_fourth_complete.lotConfiguration_corner
import kotlinx.android.synthetic.main.activity_fourth_complete.lotConfiguration_cul_de_sac
import kotlinx.android.synthetic.main.activity_fourth_complete.lotConfiguration_frontage_on_2
import kotlinx.android.synthetic.main.activity_fourth_complete.lotConfiguration_frontage_on_3
import kotlinx.android.synthetic.main.activity_fourth_complete.lotConfiguration_inside
import kotlinx.android.synthetic.main.activity_fourth_complete.pavedDrive_gravel
import kotlinx.android.synthetic.main.activity_fourth_complete.pavedDrive_partial
import kotlinx.android.synthetic.main.activity_fourth_complete.pavedDrive_paved
import kotlinx.android.synthetic.main.activity_fourth_complete.toolbar_fourth_complete
import org.json.JSONException
import org.json.JSONObject

class FourthCompleteActivity : BaseActivity() {
    private lateinit var networkUtils: NetworkUtils

    private lateinit var fourthCompleteViewModel: FourthCompleteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_complete)

        val residenceId = intent.getStringExtra("residenceId").toString()
        Log.e("residenceId" , "third is $residenceId")

        networkUtils = NetworkUtils(this@FourthCompleteActivity)

        initView()

        initChips()

        setupActionBar()
    }

    private fun initView() {
        val fourthCompleteRepository = FourthCompleteRepository(RetrofitClient.instance)
        val factory = FourthCompleteFactory(fourthCompleteRepository)
        fourthCompleteViewModel = ViewModelProvider(
            this@FourthCompleteActivity, factory)[FourthCompleteViewModel::class.java]

        btn_submit_fourth_complete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                fourthComplete()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun fourthComplete(){
        val lotConfiguration = when {
            lotConfiguration_inside.isSelected -> "Inside"
            lotConfiguration_corner.isSelected -> "Corner"
            lotConfiguration_cul_de_sac.isSelected -> "Cul De Sac"
            lotConfiguration_frontage_on_2.isSelected -> "Frontage On 2"
            lotConfiguration_frontage_on_3.isSelected -> "Frontage On 3"
            else -> ""
        }

        val landContour = when {
            landContour_level.isSelected -> "Level"
            landContour_banked.isSelected -> "Banked"
            landContour_hillside.isSelected -> "Hillside"
            landContour_depression.isSelected -> "Depression"
            else -> ""
        }

        val landSlope = when {
            landSlope_gentle.isSelected -> "Gentle"
            landSlope_moderate.isSelected -> "Moderate"
            landSlope_severe.isSelected -> "Severe"
            else -> ""
        }

        val pavedDrive = when {
            pavedDrive_paved.isSelected -> "paved"
            pavedDrive_gravel.isSelected -> "gravel"
            pavedDrive_partial.isSelected -> "partial"
            else -> ""
        }

        val poolArea = et_pool_area.text.toString().trim()
        val overallQuality = et_overall_quality.text.toString().trim()
        val overallCondition = et_overall_condition.text.toString().trim()

        val totalArea = et_total_area.text.toString().trim()
        val totalPorchSF = et_total_porch_sf.text.toString().trim()
        val lotArea = et_lot_area.text.toString().trim()

        val lotFrontage = et_lot_frontage.text.toString().trim()
        val totalSquareFeet = et_total_square_feet.text.toString().trim()
        val lowQuality = et_low_quality_finished_sf.text.toString().trim()

        val miscVal = et_misc_value.text.toString().trim()
        val houseAge = et_house_age.text.toString().trim()
        val houseRemodelAge = et_house_remodel_age.text.toString().trim()

        val token = AppReferences.getToken(this@FourthCompleteActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@FourthCompleteActivity, "Please Wait...")
            fourthCompleteViewModel.fourthComplete(
                token, residenceId,
                lotConfiguration, landContour, landSlope, pavedDrive, poolArea,
                overallQuality, overallCondition, totalArea, totalPorchSF, lotArea,
                lotFrontage, totalSquareFeet, lowQuality, miscVal, houseAge, houseRemodelAge
            )

            fourthCompleteViewModel.fourthCompleteLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("FourthCompleteActivity", "Status: $status")

                    val id = it.residence._id
                    val intent = Intent(
                        this@FourthCompleteActivity, AddListingPredictionActivity::class.java)
                    intent.putExtra("residenceId", id)
                    startActivity(intent)
                }
            }
            fourthCompleteViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@FourthCompleteActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("FourthCompleteActivity", "Fourth Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@FourthCompleteActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val lotConfiguration = when {
            lotConfiguration_inside.isSelected -> "Inside"
            lotConfiguration_corner.isSelected -> "Corner"
            lotConfiguration_cul_de_sac.isSelected -> "Cul De Sac"
            lotConfiguration_frontage_on_2.isSelected -> "Frontage On 2"
            lotConfiguration_frontage_on_3.isSelected -> "Frontage On 3"
            else -> ""
        }

        val landContour = when {
            landContour_level.isSelected -> "Level"
            landContour_banked.isSelected -> "Banked"
            landContour_hillside.isSelected -> "Hillside"
            landContour_depression.isSelected -> "Depression"
            else -> ""
        }

        val landSlope = when {
            landSlope_gentle.isSelected -> "Gentle"
            landSlope_moderate.isSelected -> "Moderate"
            landSlope_severe.isSelected -> "Severe"
            else -> ""
        }

        val pavedDrive = when {
            pavedDrive_paved.isSelected -> "paved"
            pavedDrive_gravel.isSelected -> "gravel"
            pavedDrive_partial.isSelected -> "partial"
            else -> ""
        }

        val poolArea = et_pool_area.text.toString().trim()
        val overallQuality = et_overall_quality.text.toString().trim()
        val overallCondition = et_overall_condition.text.toString().trim()

        val totalArea = et_total_area.text.toString().trim()
        val totalPorchSF = et_total_porch_sf.text.toString().trim()
        val lotArea = et_lot_area.text.toString().trim()

        val lotFrontage = et_lot_frontage.text.toString().trim()
        val totalSquareFeet = et_total_square_feet.text.toString().trim()
        val lowQuality = et_low_quality_finished_sf.text.toString().trim()

        val miscVal = et_misc_value.text.toString().trim()
        val houseAge = et_house_age.text.toString().trim()
        val houseRemodelAge = et_house_remodel_age.text.toString().trim()

        if (lotConfiguration.isEmpty()) {
            showErrorSnackBarResidence("Please Select Property Configuration", true)
            return false
        }

        if (landContour.isEmpty()) {
            showErrorSnackBarResidence("Please Select Property Flatness", true)
            return false
        }

        if (landSlope.isEmpty()) {
            showErrorSnackBarResidence("Please Select Property Slope", true)
            return false
        }

        if (pavedDrive.isEmpty()) {
            showErrorSnackBarResidence("Please Select DriveWay Type", true)
            return false
        }

        if (poolArea.isEmpty()) {
            et_pool_area.error = "Pool Area is not allowed to be empty."
            return false
        }

        if (overallQuality.isEmpty()) {
            et_overall_quality.error = "Property Quality is not allowed to be empty."
            return false
        }

        val qualityValue = overallQuality.toIntOrNull()
        if (qualityValue == null || qualityValue !in 1..10) {
            et_overall_quality.error = "Property Quality should be a number between 1 and 10."
            showErrorSnackBarResidence("Property Quality should be a number between 1 and 10.", true)
            return false
        }

        if (overallCondition.isEmpty()) {
            et_overall_condition.error = "Property Condition is not allowed to be empty."
            return false
        }

        val conditionValue = overallCondition.toIntOrNull()
        if (conditionValue == null || conditionValue !in 1..10) {
            et_overall_condition.error = "Property Condition should be a number between 1 and 10."
            showErrorSnackBarResidence(
                "Property Condition should be a number between 1 and 10.", true)
            return false
        }

        if (totalArea.isEmpty()) {
            et_total_area.error = "Total Area is not allowed to be empty."
            return false
        }

        if (totalPorchSF.isEmpty()) {
            et_total_porch_sf.error = "Total Porch SF is not allowed to be empty."
            return false
        }

        if (lotArea.isEmpty()) {
            et_lot_area.error = "Lot Area is not allowed to be empty."
            return false
        }

        if (lotFrontage.isEmpty()) {
            et_lot_frontage.error = "Lot Frontage is not allowed to be empty."
            return false
        }

        if (totalSquareFeet.isEmpty()) {
            et_total_square_feet.error = "Total Square Feet is not allowed to be empty."
            return false
        }

        if (lowQuality.isEmpty()) {
            et_low_quality_finished_sf.error = "Low Quality Finished SF is not allowed to be empty."
            return false
        }

        if (miscVal.isEmpty()) {
            et_misc_value.error = "MiscVal is not allowed to be empty."
            return false
        }

        if (houseAge.isEmpty()) {
            et_house_age.error = "House Age is not allowed to be empty."
            return false
        }

        if (houseRemodelAge.isEmpty()) {
            et_house_remodel_age.error = "House Remodel Age is not allowed to be empty."
            return false
        }

        return true
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.lotConfiguration_inside, R.id.lotConfiguration_corner,
            R.id.lotConfiguration_cul_de_sac, R.id.lotConfiguration_frontage_on_2,
            R.id.lotConfiguration_frontage_on_3,

            R.id.landContour_level, R.id.landContour_banked,
            R.id.landContour_hillside, R.id.landContour_depression,

            R.id.landSlope_gentle, R.id.landSlope_moderate, R.id.landSlope_severe,

            R.id.pavedDrive_paved, R.id.pavedDrive_gravel, R.id.pavedDrive_partial
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

    private fun setupActionBar() {
        setSupportActionBar(toolbar_fourth_complete)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_fourth_complete.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
package com.example.finalproject.ui.update_listing.activities

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
import com.example.finalproject.ui.profile.activities.EditProfileActivity
import com.example.finalproject.ui.profile.fragment.ListingsFragment
import com.example.finalproject.ui.profile.fragment.ProfileFragment
import com.example.finalproject.ui.update_listing.factory.FourthUpdateFactory
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.FourthUpdateRepository
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.FourthUpdateViewModel
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_fourth_update.btn_next_fourth_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_house_age_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_house_remodel_age_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_lot_area_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_lot_frontage_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_low_quality_finished_sf_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_misc_value_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_overall_condition_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_overall_quality_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_pool_area_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_total_area_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_total_porch_sf_update
import kotlinx.android.synthetic.main.activity_fourth_update.et_total_square_feet_update
import kotlinx.android.synthetic.main.activity_fourth_update.landContour_banked_update
import kotlinx.android.synthetic.main.activity_fourth_update.landContour_depression_update
import kotlinx.android.synthetic.main.activity_fourth_update.landContour_hillside_update
import kotlinx.android.synthetic.main.activity_fourth_update.landContour_level_update
import kotlinx.android.synthetic.main.activity_fourth_update.landSlope_gentle_update
import kotlinx.android.synthetic.main.activity_fourth_update.landSlope_moderate_update
import kotlinx.android.synthetic.main.activity_fourth_update.landSlope_severe_update
import kotlinx.android.synthetic.main.activity_fourth_update.lotConfiguration_corner_update
import kotlinx.android.synthetic.main.activity_fourth_update.lotConfiguration_cul_de_sac_update
import kotlinx.android.synthetic.main.activity_fourth_update.lotConfiguration_frontage_on_2_update
import kotlinx.android.synthetic.main.activity_fourth_update.lotConfiguration_frontage_on_3_update
import kotlinx.android.synthetic.main.activity_fourth_update.lotConfiguration_inside_update
import kotlinx.android.synthetic.main.activity_fourth_update.pavedDrive_gravel_update
import kotlinx.android.synthetic.main.activity_fourth_update.pavedDrive_partial_update
import kotlinx.android.synthetic.main.activity_fourth_update.pavedDrive_paved_update
import kotlinx.android.synthetic.main.activity_fourth_update.toolbar_fourth_update
import org.json.JSONException
import org.json.JSONObject

class FourthUpdateActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var fourthUpdateViewModel: FourthUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_update)

        networkUtils = NetworkUtils(this@FourthUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId")
        Log.e("Residence ID", residenceId.toString())

        initView()

        initChips()

        setUpActionBar()
    }

    private fun initView() {
                                    /* Get-Residence */

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@FourthUpdateActivity, factoryGetResidence)[GetResidenceViewModel::class.java]

        getResidence()

                                /* Update-Fourth-Residence */

        val fourthUpdateRepository = FourthUpdateRepository(RetrofitClient.instance)
        val factoryFourthUpdate = FourthUpdateFactory(fourthUpdateRepository)
        fourthUpdateViewModel = ViewModelProvider(
            this@FourthUpdateActivity, factoryFourthUpdate)[FourthUpdateViewModel::class.java]

        btn_next_fourth_update.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                fourthUpdate()
                showSuccessDialog()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@FourthUpdateActivity)
            val residenceId = intent.getStringExtra("residenceId").toString()

            Log.e("Residence ID", "ResidenceID: $residenceId")

            showProgressDialog(this@FourthUpdateActivity, "please wait...")
            getResidenceViewModel.getResidence(token, residenceId)

            getResidenceViewModel.getResidenceResponseLiveData.observe(
                this@FourthUpdateActivity) { response ->
                hideProgressDialog()
                response.let {
                    when (response.residence.lotConfig) {
                        "inside" -> lotConfiguration_inside_update.isChecked = true
                        "corner" -> lotConfiguration_corner_update.isChecked = true
                        "cul de sac" -> lotConfiguration_cul_de_sac_update.isChecked = true
                        "frontage on 2" -> lotConfiguration_frontage_on_2_update.isChecked = true
                        "frontage on 3" -> lotConfiguration_frontage_on_3_update.isChecked = true
                    }

                    when (response.residence.landContour) {
                        "level" -> landContour_level_update.isChecked = true
                        "banked" -> landContour_banked_update.isChecked = true
                        "hillside" -> landContour_hillside_update.isChecked = true
                        "depression" -> landContour_depression_update.isChecked = true
                    }

                    when (response.residence.landSlope) {
                        "gentle" -> landSlope_gentle_update.isChecked = true
                        "moderate" -> landSlope_moderate_update.isChecked = true
                        "severe" -> landSlope_severe_update.isChecked = true
                    }

                    when(response.residence.pavedDrive) {
                        "paved" -> pavedDrive_paved_update.isChecked = true
                        "gravel" -> pavedDrive_gravel_update.isChecked = true
                        "partial" -> pavedDrive_partial_update.isChecked = true
                    }

                    et_pool_area_update.setText(response.residence.poolArea)

                    et_overall_quality_update.setText(response.residence.overallQual)

                    et_overall_condition_update.setText(response.residence.overallCond)

                    et_total_area_update.setText(response.residence.totalarea)

                    et_total_porch_sf_update.setText(response.residence.totalporchsf)

                    et_lot_area_update.setText(response.residence.lotArea)

                    et_lot_frontage_update.setText(response.residence.lotFrontage)

                    et_total_square_feet_update.setText(response.residence.totalsf)

                    et_low_quality_finished_sf_update.setText(response.residence.lowQualFinSF)

                    et_misc_value_update.setText(response.residence.miscVal)

                    et_house_age_update.setText(response.residence.houseage)

                    et_house_remodel_age_update.setText(response.residence.houseremodelage)
                }

                getResidenceViewModel.errorLiveData.observe(this@FourthUpdateActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(
                                this@FourthUpdateActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e("FourthUpdateActivity", "Fourth Update Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@FourthUpdateActivity, error, Toast.LENGTH_LONG)
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

    private fun fourthUpdate() {
        val lotConfiguration = when {
            lotConfiguration_inside_update.isSelected -> "inside"
            lotConfiguration_corner_update.isSelected -> "corner"
            lotConfiguration_cul_de_sac_update.isSelected -> "cul de sac"
            lotConfiguration_frontage_on_2_update.isSelected -> "frontage on 2"
            lotConfiguration_frontage_on_3_update.isSelected -> "frontage on 3"
            else -> ""
        }

        val landContour = when {
            landContour_level_update.isSelected -> "level"
            landContour_banked_update.isSelected -> "banked"
            landContour_hillside_update.isSelected -> "hillside"
            landContour_depression_update.isSelected -> "depression"
            else -> ""
        }

        val landSlope = when {
            landSlope_gentle_update.isSelected -> "gentle"
            landSlope_moderate_update.isSelected -> "moderate"
            landSlope_severe_update.isSelected -> "severe"
            else -> ""
        }

        val pavedDrive = when {
            pavedDrive_paved_update.isSelected -> "paved"
            pavedDrive_gravel_update.isSelected -> "gravel"
            pavedDrive_partial_update.isSelected -> "partial"
            else -> ""
        }

        val poolArea = et_pool_area_update.text.toString().trim()
        val overallQuality = et_overall_quality_update.text.toString().trim()
        val overallCondition = et_overall_condition_update.text.toString().trim()

        val totalArea = et_total_area_update.text.toString().trim()
        val totalPorchSF = et_total_porch_sf_update.text.toString().trim()
        val lotArea = et_lot_area_update.text.toString().trim()

        val lotFrontage = et_lot_frontage_update.text.toString().trim()
        val totalSquareFeet = et_total_square_feet_update.text.toString().trim()
        val lowQuality = et_low_quality_finished_sf_update.text.toString().trim()

        val miscVal = et_misc_value_update.text.toString().trim()
        val houseAge = et_house_age_update.text.toString().trim()
        val houseRemodelAge = et_house_remodel_age_update.text.toString().trim()

        val token = AppReferences.getToken(this@FourthUpdateActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@FourthUpdateActivity, "Please Wait...")
            fourthUpdateViewModel.fourthUpdate(
                token, residenceId,
                lotConfiguration, landContour, landSlope, pavedDrive, poolArea,
                overallQuality, overallCondition, totalArea, totalPorchSF, lotArea,
                lotFrontage, totalSquareFeet, lowQuality, miscVal, houseAge, houseRemodelAge
            )

            fourthUpdateViewModel.fourthUpdateLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("FourthUpdateActivity", "Status: $status")
                }
            }
            fourthUpdateViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@FourthUpdateActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("FourthUpdateActivity", "Fourth Update Error: $errorMessage")

                    } catch (_: JSONException) {

                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val lotConfiguration = when {
            lotConfiguration_inside_update.isSelected -> "inside"
            lotConfiguration_corner_update.isSelected -> "corner"
            lotConfiguration_cul_de_sac_update.isSelected -> "cul de sac"
            lotConfiguration_frontage_on_2_update.isSelected -> "frontage on 2"
            lotConfiguration_frontage_on_3_update.isSelected -> "frontage on 3"
            else -> ""
        }

        val landContour = when {
            landContour_level_update.isSelected -> "level"
            landContour_banked_update.isSelected -> "banked"
            landContour_hillside_update.isSelected -> "hillside"
            landContour_depression_update.isSelected -> "depression"
            else -> ""
        }

        val landSlope = when {
            landSlope_gentle_update.isSelected -> "gentle"
            landSlope_moderate_update.isSelected -> "moderate"
            landSlope_severe_update.isSelected -> "severe"
            else -> ""
        }

        val pavedDrive = when {
            pavedDrive_paved_update.isSelected -> "paved"
            pavedDrive_gravel_update.isSelected -> "gravel"
            pavedDrive_partial_update.isSelected -> "partial"
            else -> ""
        }

        val poolArea = et_pool_area_update.text.toString().trim()
        val overallQuality = et_overall_quality_update.text.toString().trim()
        val overallCondition = et_overall_condition_update.text.toString().trim()

        val totalArea = et_total_area_update.text.toString().trim()
        val totalPorchSF = et_total_porch_sf_update.text.toString().trim()
        val lotArea = et_lot_area_update.text.toString().trim()

        val lotFrontage = et_lot_frontage_update.text.toString().trim()
        val totalSquareFeet = et_total_square_feet_update.text.toString().trim()
        val lowQuality = et_low_quality_finished_sf_update.text.toString().trim()

        val miscVal = et_misc_value_update.text.toString().trim()
        val houseAge = et_house_age_update.text.toString().trim()
        val houseRemodelAge = et_house_remodel_age_update.text.toString().trim()

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
            et_pool_area_update.error = "Pool Area is not allowed to be empty."
            return false
        }

        if (overallQuality.isEmpty()) {
            et_overall_quality_update.error = "Property Quality is not allowed to be empty."
            return false
        }

        val qualityValue = overallQuality.toIntOrNull()
        if (qualityValue == null || qualityValue !in 1..10) {
            et_overall_quality_update.error = "Property Quality should be a number between 1 and 10."
            showErrorSnackBarResidence("Property Quality should be a number between 1 and 10.", true)
            return false
        }

        if (overallCondition.isEmpty()) {
            et_overall_condition_update.error = "Property Condition is not allowed to be empty."
            return false
        }

        val conditionValue = overallCondition.toIntOrNull()
        if (conditionValue == null || conditionValue !in 1..10) {
            et_overall_condition_update.error = "Property Condition should be a number between 1 and 10."
            showErrorSnackBarResidence(
                "Property Condition should be a number between 1 and 10.", true)
            return false
        }

        if (totalArea.isEmpty()) {
            et_total_area_update.error = "Total Area is not allowed to be empty."
            return false
        }

        if (totalPorchSF.isEmpty()) {
            et_total_porch_sf_update.error = "Total Porch SF is not allowed to be empty."
            return false
        }

        if (lotArea.isEmpty()) {
            et_lot_area_update.error = "Lot Area is not allowed to be empty."
            return false
        }

        if (lotFrontage.isEmpty()) {
            et_lot_frontage_update.error = "Lot Frontage is not allowed to be empty."
            return false
        }

        if (totalSquareFeet.isEmpty()) {
            et_total_square_feet_update.error = "Total Square Feet is not allowed to be empty."
            return false
        }

        if (lowQuality.isEmpty()) {
            et_low_quality_finished_sf_update.error = "Low Quality Finished SF is not allowed to be empty."
            return false
        }

        if (miscVal.isEmpty()) {
            et_misc_value_update.error = "MiscVal is not allowed to be empty."
            return false
        }

        if (houseAge.isEmpty()) {
            et_house_age_update.error = "House Age is not allowed to be empty."
            return false
        }

        if (houseRemodelAge.isEmpty()) {
            et_house_remodel_age_update.error = "House Remodel Age is not allowed to be empty."
            return false
        }

        return true
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.lotConfiguration_inside_update, R.id.lotConfiguration_corner_update,
            R.id.lotConfiguration_cul_de_sac_update, R.id.lotConfiguration_frontage_on_2_update,
            R.id.lotConfiguration_frontage_on_3_update,

            R.id.landContour_level_update, R.id.landContour_banked_update,
            R.id.landContour_hillside_update, R.id.landContour_depression_update,

            R.id.landSlope_gentle_update, R.id.landSlope_moderate_update, R.id.landSlope_severe_update,

            R.id.pavedDrive_paved_update, R.id.pavedDrive_gravel_update, R.id.pavedDrive_partial_update
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

    private fun showSuccessDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        val messageDialogView =
            layoutInflater.inflate(R.layout.edit_list_success, null)

        messageDialogView.background =
            ContextCompat.getDrawable(this, R.drawable.message_options_dialog_background)

        val closeButton = messageDialogView.findViewById<TextView>(R.id.close_edit_list_success)

        closeButton.setOnClickListener {
            // TODO - Navigate to the next screen
        }

        bottomSheetDialog.setContentView(messageDialogView)
        bottomSheetDialog.show()
    }


    private fun setUpActionBar() {
        setSupportActionBar(toolbar_fourth_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_fourth_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
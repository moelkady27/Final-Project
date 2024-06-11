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
import com.example.finalproject.ui.update_listing.factory.FirstUpdateFactory
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.FirstUpdateRepository
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.FirstUpdateViewModel
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_building_type_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_electrical_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_neighborhood_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_sale_type_update
import kotlinx.android.synthetic.main.activity_first_update.btn_next_first_update
import kotlinx.android.synthetic.main.activity_first_update.et_month_of_sold_update
import kotlinx.android.synthetic.main.activity_first_update.et_sell_price_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_brick_and_tile_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_cinder_block_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_poured_contrete_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_slab_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_stone_update
import kotlinx.android.synthetic.main.activity_first_update.foundation_wood_update
import kotlinx.android.synthetic.main.activity_first_update.lot_shape_irregular_update
import kotlinx.android.synthetic.main.activity_first_update.lot_shape_moderately_update
import kotlinx.android.synthetic.main.activity_first_update.lot_shape_regular_update
import kotlinx.android.synthetic.main.activity_first_update.lot_shape_slightly_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_agricultural_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_commercial_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_floating_village_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_high_residential_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_industrial_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_low_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_medium_update
import kotlinx.android.synthetic.main.activity_first_update.msZoning_park_update
import kotlinx.android.synthetic.main.activity_first_update.payment_period_monthly_update
import kotlinx.android.synthetic.main.activity_first_update.payment_period_yearly_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_abnormal_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_adjoining_land_purchase_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_allocation_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_family_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_normal_update
import kotlinx.android.synthetic.main.activity_first_update.sale_condition_partial_update
import kotlinx.android.synthetic.main.activity_first_update.toolbar_first_update
import kotlinx.android.synthetic.main.activity_first_update.utilities_electricity_update
import kotlinx.android.synthetic.main.activity_first_update.utilities_gas_update
import kotlinx.android.synthetic.main.activity_first_update.utilities_water_update
import org.json.JSONException
import org.json.JSONObject

class FirstUpdateActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var getResidenceViewModel: GetResidenceViewModel

    private lateinit var firstUpdateViewModel: FirstUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_update)

        networkUtils = NetworkUtils(this@FirstUpdateActivity)

        val residenceId = intent.getStringExtra("residence_id")
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
            this@FirstUpdateActivity, factoryGetResidence)[GetResidenceViewModel::class.java]

        getResidence()

                                /* Update-First-Residence */

        val firstUpdateRepository = FirstUpdateRepository(RetrofitClient.instance)
        val factoryFirstUpdate = FirstUpdateFactory(firstUpdateRepository)
        firstUpdateViewModel = ViewModelProvider(
            this@FirstUpdateActivity, factoryFirstUpdate)[FirstUpdateViewModel::class.java]

        btn_next_first_update.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                firstUpdate()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun getResidence() {
        if(networkUtils.isNetworkAvailable()) {
            val token = AppReferences.getToken(this@FirstUpdateActivity)
            val residenceId = intent.getStringExtra("residence_id").toString()

            Log.e("Residence ID", "ResidenceID: $residenceId")

            showProgressDialog(this@FirstUpdateActivity, "please wait...")
            getResidenceViewModel.getResidence(token, residenceId)

            getResidenceViewModel.getResidenceResponseLiveData.observe(this@FirstUpdateActivity) { response ->
                hideProgressDialog()
                response.let {
                    auto_complete_neighborhood_update.setText(it.residence.neighborhood)

                    when (response.residence.mszoning) {
                        "agricultural" -> msZoning_agricultural_update.isChecked = true
                        "commercial" -> msZoning_commercial_update.isChecked = true
                        "floating village" -> msZoning_floating_village_update.isChecked = true
                        "industrial" -> msZoning_industrial_update.isChecked = true
                        "high residential" -> msZoning_high_residential_update.isChecked = true
                        "low" -> msZoning_low_update.isChecked = true
                        "park" -> msZoning_park_update.isChecked = true
                        "medium" -> msZoning_medium_update.isChecked = true
                    }

                    when (response.residence.saleCondition) {
                        "normal" -> sale_condition_normal_update.isChecked = true
                        "abnormal" -> sale_condition_abnormal_update.isChecked = true
                        "adjoining land purchase" -> sale_condition_adjoining_land_purchase_update.isChecked = true
                        "allocation" -> sale_condition_allocation_update.isChecked = true
                        "family" -> sale_condition_family_update.isChecked = true
                        "partial" -> sale_condition_partial_update.isChecked = true
                    }

                    et_month_of_sold_update.setText(response.residence.moSold)

                    et_sell_price_update.setText(response.residence.salePrice)

                    when (response.residence.paymentPeriod) {
                        "monthly" -> {
                            payment_period_monthly_update.isChecked = true
                            payment_period_yearly_update.isChecked = false
                        }

                        "yearly" -> {
                            payment_period_yearly_update.isChecked = true
                            payment_period_monthly_update.isChecked = false
                        }
                    }

                    auto_complete_sale_type_update.setText(response.residence.saleType)

                    when (response.residence.utilities) {
                        "NoSewr" -> {
                            utilities_gas_update.isChecked = true
                            utilities_electricity_update.isChecked = true
                            utilities_water_update.isChecked = true
                        }

                        "NoSeWa" -> {
                            utilities_gas_update.isChecked = true
                            utilities_electricity_update.isChecked = true
                            utilities_water_update.isChecked = false
                        }

                        "ELO" -> {
                            utilities_gas_update.isChecked = false
                            utilities_electricity_update.isChecked = true
                            utilities_water_update.isChecked = false
                        }

                        "AllPub" -> {
                            utilities_gas_update.isChecked = true
                            utilities_electricity_update.isChecked = true
                            utilities_water_update.isChecked = true
                        }
                    }

                    when (response.residence.lotShape) {
                        "regular" -> lot_shape_regular_update.isChecked = true
                        "slightly" -> lot_shape_slightly_update.isChecked = true
                        "moderately" -> lot_shape_moderately_update.isChecked = true
                        "irregular" -> lot_shape_irregular_update.isChecked = true
                    }

                    auto_complete_electrical_update.setText(response.residence.electrical)

                    when (response.residence.foundation) {
                        "slab" -> foundation_slab_update.isChecked = true
                        "stone" -> foundation_stone_update.isChecked = true
                        "wood" -> foundation_wood_update.isChecked = true
                        "cinder block" -> foundation_cinder_block_update.isChecked = true
                        "brick and tile" -> foundation_brick_and_tile_update.isChecked = true
                        "poured contrete" -> foundation_poured_contrete_update.isChecked = true
                    }

                    auto_complete_building_type_update.setText(response.residence.bldgType)

                }

                getResidenceViewModel.errorLiveData.observe(this@FirstUpdateActivity) { error ->
                    hideProgressDialog()
                    error?.let {
                        try {
                            val errorMessage = JSONObject(error).getString("message")
                            Toast.makeText(
                                this@FirstUpdateActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e("FirstUpdateActivity", "First Update Error: $errorMessage")

                        } catch (e: JSONException) {
                            Toast.makeText(this@FirstUpdateActivity, error, Toast.LENGTH_LONG)
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

    private fun firstUpdate() {
        val neighborhood = auto_complete_neighborhood_update.text.toString().trim()
        val msZoning = when {
            msZoning_agricultural_update.isSelected -> "agricultural"
            msZoning_commercial_update.isSelected -> "commercial"
            msZoning_floating_village_update.isSelected -> "floating village"
            msZoning_industrial_update.isSelected -> "industrial"
            msZoning_high_residential_update.isSelected -> "high residential"
            msZoning_low_update.isSelected -> "low"
            msZoning_park_update.isSelected -> "park"
            msZoning_medium_update.isSelected -> "medium"
            else -> ""
        }
        val saleCondition = when {
            sale_condition_normal_update.isSelected -> "normal"
            sale_condition_abnormal_update.isSelected -> "abnormal"
            sale_condition_adjoining_land_purchase_update.isSelected -> "adjoining land purchase"
            sale_condition_allocation_update.isSelected -> "allocation"
            sale_condition_family_update.isSelected -> "family"
            sale_condition_partial_update.isSelected -> "partial"
            else -> ""
        }
        val etMonthOfSold = et_month_of_sold_update.text.toString().trim()
        val sellPrice = et_sell_price_update.text.toString().trim()
        val paymentPeriod = when {
            payment_period_monthly_update.isSelected -> "monthly"
            payment_period_yearly_update.isSelected -> "yearly"
            else -> ""
        }
        val saleType = auto_complete_sale_type_update.text.toString().trim()

        val utilitiesList = mutableListOf<String>()
        val utilitiesChips = listOf(
            utilities_gas_update, utilities_electricity_update, utilities_water_update
        )
        utilitiesChips.forEach { chip ->
            if (chip.isSelected) {
                utilitiesList.add(chip.text.toString())
            }
        }

        val lotShape = when {
            lot_shape_regular_update.isSelected -> "regular"
            lot_shape_slightly_update.isSelected -> "slightly"
            lot_shape_moderately_update.isSelected -> "moderately"
            lot_shape_irregular_update.isSelected -> "irregular"
            else -> ""
        }
        val electrical = auto_complete_electrical_update.text.toString().trim()
        val foundation = when {
            foundation_slab_update.isSelected -> "slab"
            foundation_stone_update.isSelected -> "stone"
            foundation_wood_update.isSelected -> "wood"
            foundation_cinder_block_update.isSelected -> "cinder block"
            foundation_brick_and_tile_update.isSelected -> "brick and tile"
            foundation_poured_contrete_update.isSelected -> "poured contrete"
            else -> ""
        }
        val buildingType = auto_complete_building_type_update.text.toString().trim()

        val token = AppReferences.getToken(this@FirstUpdateActivity)

        val residenceId = intent.getStringExtra("residence_id").toString()

        if (isValidInput()) {
            showProgressDialog(this@FirstUpdateActivity, "Please Wait...")
            firstUpdateViewModel.firstUpdate(
                token, residenceId, neighborhood, msZoning, saleCondition,
                etMonthOfSold, sellPrice, paymentPeriod, saleType,
                utilitiesList, lotShape, electrical, foundation, buildingType
            )

            firstUpdateViewModel.firstUpdateLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("FirstUpdateActivity", "Status: $status")

                    val intent = Intent(
                        this@FirstUpdateActivity, SecondUpdateActivity::class.java)
                    intent.putExtra("residenceId", residenceId)
                    Log.e("residenceId" , "First Update is $residenceId")
                    startActivity(intent)
                }
            }
            firstUpdateViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@FirstUpdateActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("FirstUpdateActivity", "First Update Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@FirstUpdateActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val neighborhood = auto_complete_neighborhood_update.text.toString().trim()
        val msZoning = when {
            msZoning_agricultural_update.isSelected -> "agricultural"
            msZoning_commercial_update.isSelected -> "commercial"
            msZoning_floating_village_update.isSelected -> "floating village"
            msZoning_industrial_update.isSelected -> "industrial"
            msZoning_high_residential_update.isSelected -> "high residential"
            msZoning_low_update.isSelected -> "low"
            msZoning_park_update.isSelected -> "park"
            msZoning_medium_update.isSelected -> "medium"
            else -> ""
        }
        val saleCondition = when {
            sale_condition_normal_update.isSelected -> "normal"
            sale_condition_abnormal_update.isSelected -> "abnormal"
            sale_condition_adjoining_land_purchase_update.isSelected -> "adjoining land purchase"
            sale_condition_allocation_update.isSelected -> "allocation"
            sale_condition_family_update.isSelected -> "family"
            sale_condition_partial_update.isSelected -> "partial"
            else -> ""
        }
        val etMonthOfSold = et_month_of_sold_update.text.toString().trim()
        val sellPrice = et_sell_price_update.text.toString().trim()
        val paymentPeriod = when {
            payment_period_monthly_update.isSelected -> "monthly"
            payment_period_yearly_update.isSelected -> "yearly"
            else -> ""
        }
        val saleType = auto_complete_sale_type_update.text.toString().trim()

        val utilitiesList = mutableListOf<String>()
        val utilitiesChips = listOf(
            utilities_gas_update, utilities_electricity_update, utilities_water_update
        )
        utilitiesChips.forEach { chip ->
            if (chip.isSelected) {
                utilitiesList.add(chip.text.toString())
            }
        }

        val lotShape = when {
            lot_shape_regular_update.isSelected -> "regular"
            lot_shape_slightly_update.isSelected -> "slightly"
            lot_shape_moderately_update.isSelected -> "moderately"
            lot_shape_irregular_update.isSelected -> "irregular"
            else -> ""
        }
        val electrical = auto_complete_electrical_update.text.toString().trim()
        val foundation = when {
            foundation_slab_update.isSelected -> "slab"
            foundation_stone_update.isSelected -> "stone"
            foundation_wood_update.isSelected -> "wood"
            foundation_cinder_block_update.isSelected -> "cinder block"
            foundation_brick_and_tile_update.isSelected -> "brick and tile"
            foundation_poured_contrete_update.isSelected -> "poured contrete"
            else -> ""
        }
        val buildingType = auto_complete_building_type_update.text.toString().trim()

        val neighborhoodItems = resources.getStringArray(R.array.neighborhood_items)
        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)

        if (neighborhood.isEmpty()) {
            auto_complete_neighborhood_update.error = "Neighborhood is not allowed to be empty."
            return false
        }

        if (msZoning.isEmpty()) {
            showErrorSnackBarResidence("Please Select MsZoning", true)
            return false
        }

        if (saleCondition.isEmpty()) {
            showErrorSnackBarResidence("Please Select Sale Condition", true)
            return false
        }

        if (etMonthOfSold.isEmpty()) {
            et_month_of_sold_update.error = "Month Of Sold is not allowed to be empty."
            showErrorSnackBarResidence("Month Of Sold is not allowed to be empty.", true)
            return false
        }

        val monthSold = etMonthOfSold.toIntOrNull()
        if (monthSold == null || monthSold !in 1..12) {
            et_month_of_sold_update.error = "Please enter a number between 1 and 12."
            showErrorSnackBarResidence("Please enter a number between 1 and 12.", true)
            return false
        }

        if (sellPrice.isEmpty()) {
            et_sell_price_update.error = "Sell Price is not allowed to be empty."
            Toast.makeText(this@FirstUpdateActivity,
                "Sell Price is not allowed to be empty.", Toast.LENGTH_LONG).show()
            return false
        }

        if (paymentPeriod.isEmpty()) {
            showErrorSnackBarResidence("Please Select Payment Period", true)
            return false
        }

        if (saleType.isEmpty()) {
            auto_complete_sale_type_update.error = "Sale Type is not allowed to be empty."
            return false
        }

        if (utilitiesList.isEmpty()) {
            showErrorSnackBarResidence("Please Select Utilities", true)
            return false
        }

        if (lotShape.isEmpty()) {
            showErrorSnackBarResidence("Please Select Lot Shape", true)
            return false
        }

        if (electrical.isEmpty()) {
            auto_complete_electrical_update.error = "Electrical is not allowed to be empty."
            return false
        }

        if (foundation.isEmpty()) {
            showErrorSnackBarResidence("Please Select Foundation", true)
            return false
        }

        if (buildingType.isEmpty()) {
            auto_complete_building_type_update.error = "Building Type is not allowed to be empty."
            return false
        }

        if (!neighborhoodItems.any {
            it.equals(auto_complete_neighborhood_update.text.toString().trim(), ignoreCase = true) }) {
            auto_complete_neighborhood_update.error = "Please select a valid neighborhood"
            return false
        }

        if (!saleTypeItems.any {
            it.equals(auto_complete_sale_type_update.text.toString().trim(), ignoreCase = true) }) {
            auto_complete_sale_type_update.error = "Please select a valid sale type"
            return false
        }

        if (!electricalItems.any {
            it.equals(auto_complete_electrical_update.text.toString().trim(), ignoreCase = true) }) {
            auto_complete_electrical_update.error = "Please select a valid electrical option"
            return false
        }

        if (!buildingTypeItems.any {
            it.equals(auto_complete_building_type_update.text.toString().trim(), ignoreCase = true) }) {
            auto_complete_building_type_update.error = "Please select a valid building type"
            return false
        }

        return true
    }

    private fun initDropDowns() {
        val neighborhoodItems = resources.getStringArray(R.array.neighborhood_items)
        val arrayNeighborhoodAdapter = ArrayAdapter(
            this@FirstUpdateActivity, R.layout.dropdown_item, neighborhoodItems
        )
        auto_complete_neighborhood_update.setAdapter(arrayNeighborhoodAdapter)
        auto_complete_neighborhood_update.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedNeighborhoodUpdate", "Selected Neighborhood Update: $selectedItem")
        }

        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val arraySaleTypeAdapter = ArrayAdapter(
            this@FirstUpdateActivity, R.layout.dropdown_item, saleTypeItems
        )
        auto_complete_sale_type_update.setAdapter(arraySaleTypeAdapter)
        auto_complete_sale_type_update.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedSaleTypeUpdate", "Selected SaleType Update: $selectedItem")
        }

        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val arrayElectricalAdapter = ArrayAdapter(
            this@FirstUpdateActivity, R.layout.dropdown_item, electricalItems
        )
        auto_complete_electrical_update.setAdapter(arrayElectricalAdapter)
        auto_complete_electrical_update.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedElectricalUpdate", "Selected Electrical Update: $selectedItem")
        }

        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)
        val arrayBuildingTypeAdapter = ArrayAdapter(
            this@FirstUpdateActivity, R.layout.dropdown_item, buildingTypeItems
        )
        auto_complete_building_type_update.setAdapter(arrayBuildingTypeAdapter)
        auto_complete_building_type_update.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedBuildingTypeUpdate", "Selected BuildingType Update: $selectedItem")

        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.msZoning_agricultural_update, R.id.msZoning_commercial_update,
            R.id.msZoning_floating_village_update, R.id.msZoning_industrial_update,
            R.id.msZoning_high_residential_update, R.id.msZoning_low_update,
            R.id.msZoning_park_update, R.id.msZoning_medium_update,

            R.id.sale_condition_normal_update, R.id.sale_condition_abnormal_update,
            R.id.sale_condition_adjoining_land_purchase_update, R.id.sale_condition_allocation_update,
            R.id.sale_condition_family_update, R.id.sale_condition_partial_update,

            R.id.payment_period_monthly_update, R.id.payment_period_yearly_update,

            R.id.utilities_gas_update, R.id.utilities_electricity_update, R.id.utilities_water_update,

            R.id.lot_shape_regular_update, R.id.lot_shape_slightly_update,
            R.id.lot_shape_moderately_update, R.id.lot_shape_irregular_update,

            R.id.foundation_slab_update, R.id.foundation_stone_update,
            R.id.foundation_wood_update, R.id.foundation_cinder_block_update,
            R.id.foundation_brick_and_tile_update, R.id.foundation_poured_contrete_update
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
        setSupportActionBar(toolbar_first_update)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            actionBar.title = ""
        }

        toolbar_first_update.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
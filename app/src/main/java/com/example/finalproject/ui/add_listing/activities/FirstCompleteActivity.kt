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
import com.example.finalproject.ui.add_listing.factory.FirstCompleteFactory
import com.example.finalproject.ui.add_listing.repository.FirstCompleteRepository
import com.example.finalproject.ui.add_listing.viewModel.FirstCompleteViewModel
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_first_complete.ETMonthOfSold
import kotlinx.android.synthetic.main.activity_first_complete.ETSellPrice
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteBuildingType
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteElectrical
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteNeighborhood
import kotlinx.android.synthetic.main.activity_first_complete.autoCompleteSaleType
import kotlinx.android.synthetic.main.activity_first_complete.btn_next_first_complete
import kotlinx.android.synthetic.main.activity_first_complete.foundation_brick_and_tile
import kotlinx.android.synthetic.main.activity_first_complete.foundation_cinder_block
import kotlinx.android.synthetic.main.activity_first_complete.foundation_poured_contrete
import kotlinx.android.synthetic.main.activity_first_complete.foundation_slab
import kotlinx.android.synthetic.main.activity_first_complete.foundation_stone
import kotlinx.android.synthetic.main.activity_first_complete.foundation_wood
import kotlinx.android.synthetic.main.activity_first_complete.lot_shape_irregular
import kotlinx.android.synthetic.main.activity_first_complete.lot_shape_moderately
import kotlinx.android.synthetic.main.activity_first_complete.lot_shape_regular
import kotlinx.android.synthetic.main.activity_first_complete.lot_shape_slightly
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_agricultural
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_commercial
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_floating_village
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_high_residential
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_industrial
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_low
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_medium
import kotlinx.android.synthetic.main.activity_first_complete.msZoning_park
import kotlinx.android.synthetic.main.activity_first_complete.payment_period_monthly
import kotlinx.android.synthetic.main.activity_first_complete.payment_period_yearly
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_abnormal
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_adjoining_land_purchase
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_allocation
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_family
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_normal
import kotlinx.android.synthetic.main.activity_first_complete.sale_condition_partial
import kotlinx.android.synthetic.main.activity_first_complete.toolbar_add_listing_second
import kotlinx.android.synthetic.main.activity_first_complete.utilities_electricity
import kotlinx.android.synthetic.main.activity_first_complete.utilities_gas
import kotlinx.android.synthetic.main.activity_first_complete.utilities_water
import org.json.JSONException
import org.json.JSONObject

class FirstCompleteActivity : BaseActivity() {

    private lateinit var networkUtils: NetworkUtils

    private lateinit var firstCompleteViewModel: FirstCompleteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_complete)

        networkUtils = NetworkUtils(this@FirstCompleteActivity)

        initView()

        initDropDowns()

        initChips()

        setupActionBar()
    }

    private fun initView() {
        val firstCompleteRepository = FirstCompleteRepository(RetrofitClient.instance)
        val factory = FirstCompleteFactory(firstCompleteRepository)
        firstCompleteViewModel = ViewModelProvider(
            this@FirstCompleteActivity, factory)[FirstCompleteViewModel::class.java]

        btn_next_first_complete.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                firstComplete()
            } else {
                showErrorSnackBar("No internet connection", true)
            }
        }
    }

    private fun firstComplete(){
        val neighborhood = autoCompleteNeighborhood.text.toString().trim()
        val msZoning = when {
            msZoning_agricultural.isSelected -> "Agricultural"
            msZoning_commercial.isSelected -> "Commercial"
            msZoning_floating_village.isSelected -> "Floating Village"
            msZoning_industrial.isSelected -> "Industrial"
            msZoning_high_residential.isSelected -> "High Residential"
            msZoning_low.isSelected -> "Low"
            msZoning_park.isSelected -> "Park"
            msZoning_medium.isSelected -> "Medium"
            else -> ""
        }
        val saleCondition = when {
            sale_condition_normal.isSelected -> "Normal"
            sale_condition_abnormal.isSelected -> "Abnormal"
            sale_condition_adjoining_land_purchase.isSelected -> "Adjoining Land Purchase"
            sale_condition_allocation.isSelected -> "Allocation"
            sale_condition_family.isSelected -> "Family"
            sale_condition_partial.isSelected -> "Partial"
            else -> ""
        }
        val etMonthOfSold = ETMonthOfSold.text.toString().trim()
        val sellPrice = ETSellPrice.text.toString().trim()
        val paymentPeriod = when {
            payment_period_monthly.isSelected -> "Monthly"
            payment_period_yearly.isSelected -> "Yearly"
            else -> ""
        }
        val saleType = autoCompleteSaleType.text.toString().trim()

        val utilitiesList = mutableListOf<String>()
        val utilitiesChips = listOf(utilities_gas, utilities_electricity, utilities_water)
        utilitiesChips.forEach { chip ->
            if (chip.isSelected) {
                utilitiesList.add(chip.text.toString())
            }
        }

        val lotShape = when {
            lot_shape_regular.isSelected -> "Regular"
            lot_shape_slightly.isSelected -> "Slightly"
            lot_shape_moderately.isSelected -> "Moderately"
            lot_shape_irregular.isSelected -> "Irregular"
            else -> ""
        }
        val electrical = autoCompleteElectrical.text.toString().trim()
        val foundation = when {
            foundation_slab.isSelected -> "Slab"
            foundation_stone.isSelected -> "Stone"
            foundation_wood.isSelected -> "Wood"
            foundation_cinder_block.isSelected -> "Cinder Block"
            foundation_brick_and_tile.isSelected -> "Brick and Tile"
            foundation_poured_contrete.isSelected -> "Poured Contrete"
            else -> ""
        }
        val buildingType = autoCompleteBuildingType.text.toString().trim()

        val token = AppReferences.getToken(this@FirstCompleteActivity)

        val residenceId = intent.getStringExtra("residenceId").toString()

        if (isValidInput()) {
            showProgressDialog(this@FirstCompleteActivity, "Please Wait...")
            firstCompleteViewModel.firstComplete(
                token, residenceId, neighborhood, msZoning, saleCondition,
                etMonthOfSold, sellPrice, paymentPeriod, saleType,
                utilitiesList, lotShape, electrical, foundation, buildingType
            )

            firstCompleteViewModel.firstCompleteLiveData.observe(this) { response ->
                hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("FirstCompleteActivity", "Status: $status")
                }
            }
            firstCompleteViewModel.errorLiveData.observe(this) { error ->
                hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            this@FirstCompleteActivity, errorMessage, Toast.LENGTH_LONG).show()

                        Log.e("FirstCompleteActivity", "First Complete Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@FirstCompleteActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val neighborhood = autoCompleteNeighborhood.text.toString().trim()
        val msZoning = when {
            msZoning_agricultural.isSelected -> "Agricultural"
            msZoning_commercial.isSelected -> "Commercial"
            msZoning_floating_village.isSelected -> "Floating Village"
            msZoning_industrial.isSelected -> "Industrial"
            msZoning_high_residential.isSelected -> "High Residential"
            msZoning_low.isSelected -> "Low"
            msZoning_park.isSelected -> "Park"
            msZoning_medium.isSelected -> "Medium"
            else -> ""
        }
        val saleCondition = when {
            sale_condition_normal.isSelected -> "Normal"
            sale_condition_abnormal.isSelected -> "Abnormal"
            sale_condition_adjoining_land_purchase.isSelected -> "Adjoining Land Purchase"
            sale_condition_allocation.isSelected -> "Allocation"
            sale_condition_family.isSelected -> "Family"
            sale_condition_partial.isSelected -> "Partial"
            else -> ""
        }
        val etMonthOfSold = ETMonthOfSold.text.toString().trim()
        val sellPrice = ETSellPrice.text.toString().trim()
        val paymentPeriod = when {
            payment_period_monthly.isSelected -> "Monthly"
            payment_period_yearly.isSelected -> "Yearly"
            else -> ""
        }
        val saleType = autoCompleteSaleType.text.toString().trim()

        val utilitiesList = mutableListOf<String>()
        val utilitiesChips = listOf(utilities_gas, utilities_electricity, utilities_water)
        utilitiesChips.forEach { chip ->
            if (chip.isSelected) {
                utilitiesList.add(chip.text.toString())
            }
        }

        val lotShape = when {
            lot_shape_regular.isSelected -> "Regular"
            lot_shape_slightly.isSelected -> "Slightly"
            lot_shape_moderately.isSelected -> "Moderately"
            lot_shape_irregular.isSelected -> "Irregular"
            else -> ""
        }
        val electrical = autoCompleteElectrical.text.toString().trim()
        val foundation = when {
            foundation_slab.isSelected -> "Slab"
            foundation_stone.isSelected -> "Stone"
            foundation_wood.isSelected -> "Wood"
            foundation_cinder_block.isSelected -> "Cinder Block"
            foundation_brick_and_tile.isSelected -> "Brick and Tile"
            foundation_poured_contrete.isSelected -> "Poured Contrete"
            else -> ""
        }
        val buildingType = autoCompleteBuildingType.text.toString().trim()

        val neighborhoodItems = resources.getStringArray(R.array.neighborhood_items)
        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)

        if (neighborhood.isEmpty()) {
            autoCompleteNeighborhood.error = "Neighborhood is not allowed to be empty."
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
            ETMonthOfSold.error = "Month Of Sold is not allowed to be empty."
            showErrorSnackBarResidence("Month Of Sold is not allowed to be empty.", true)
            return false
        }

        val monthSold = etMonthOfSold.toIntOrNull()
        if (monthSold == null || monthSold !in 1..12) {
            ETMonthOfSold.error = "Please enter a number between 1 and 12."
            showErrorSnackBarResidence("Please enter a number between 1 and 12.", true)
            return false
        }

        if (sellPrice.isEmpty()) {
            ETSellPrice.error = "Sell Price is not allowed to be empty."
            Toast.makeText(this@FirstCompleteActivity,
                "Sell Price is not allowed to be empty.", Toast.LENGTH_LONG).show()
            return false
        }

        if (paymentPeriod.isEmpty()) {
            showErrorSnackBarResidence("Please Select Payment Period", true)
            return false
        }

        if (saleType.isEmpty()) {
            autoCompleteSaleType.error = "Sale Type is not allowed to be empty."
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
            autoCompleteElectrical.error = "Electrical is not allowed to be empty."
            return false
        }

        if (foundation.isEmpty()) {
            showErrorSnackBarResidence("Please Select Foundation", true)
            return false
        }

        if (buildingType.isEmpty()) {
            autoCompleteBuildingType.error = "Building Type is not allowed to be empty."
            return false
        }

        if (!neighborhoodItems.contains(autoCompleteNeighborhood.text.toString().trim())) {
            autoCompleteNeighborhood.error = "Please select a valid neighborhood"
            return false
        }

        if (!saleTypeItems.contains(autoCompleteSaleType.text.toString().trim())) {
            autoCompleteSaleType.error = "Please select a valid sale type"
            return false
        }

        if (!electricalItems.contains(autoCompleteElectrical.text.toString().trim())) {
            autoCompleteElectrical.error = "Please select a valid electrical option"
            return false
        }

        if (!buildingTypeItems.contains(autoCompleteBuildingType.text.toString().trim())) {
            autoCompleteBuildingType.error = "Please select a valid building type"
            return false
        }

        return true
    }

    private fun initDropDowns() {
        val neighborhoodItems = resources.getStringArray(R.array.neighborhood_items)
        val arrayNeighborhoodAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, neighborhoodItems
        )
        autoCompleteNeighborhood.setAdapter(arrayNeighborhoodAdapter)
        autoCompleteNeighborhood.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedNeighborhood", "Selected Neighborhood: $selectedItem")
        }

        val saleTypeItems = resources.getStringArray(R.array.sale_type_items)
        val arraySaleTypeAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, saleTypeItems
        )
        autoCompleteSaleType.setAdapter(arraySaleTypeAdapter)
        autoCompleteSaleType.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedSaleType", "Selected SaleType: $selectedItem")
        }

        val electricalItems = resources.getStringArray(R.array.electrical_items)
        val arrayElectricalAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, electricalItems
        )
        autoCompleteElectrical.setAdapter(arrayElectricalAdapter)
        autoCompleteElectrical.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedElectrical", "Selected Electrical: $selectedItem")
        }

        val buildingTypeItems = resources.getStringArray(R.array.building_type_items)
        val arrayBuildingTypeAdapter = ArrayAdapter(
            this@FirstCompleteActivity, R.layout.dropdown_item, buildingTypeItems
        )
        autoCompleteBuildingType.setAdapter(arrayBuildingTypeAdapter)
        autoCompleteBuildingType.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.e("SelectedBuildingType", "Selected BuildingType: $selectedItem")

        }
    }

    private fun initChips(){
        val chipIds = listOf(
            R.id.msZoning_agricultural, R.id.msZoning_commercial,
            R.id.msZoning_floating_village, R.id.msZoning_industrial,
            R.id.msZoning_high_residential, R.id.msZoning_low,
            R.id.msZoning_park, R.id.msZoning_medium,

            R.id.sale_condition_normal, R.id.sale_condition_abnormal,
            R.id.sale_condition_adjoining_land_purchase, R.id.sale_condition_allocation,
            R.id.sale_condition_family, R.id.sale_condition_partial,

            R.id.payment_period_monthly, R.id.payment_period_yearly,

            R.id.utilities_gas, R.id.utilities_electricity, R.id.utilities_water,

            R.id.lot_shape_regular, R.id.lot_shape_slightly,
            R.id.lot_shape_moderately, R.id.lot_shape_irregular,

            R.id.foundation_slab, R.id.foundation_stone,
            R.id.foundation_wood, R.id.foundation_cinder_block,
            R.id.foundation_brick_and_tile, R.id.foundation_poured_contrete
        )

        chipIds.forEach { chipId ->
            findViewById<Chip>(chipId).setOnCheckedChangeListener { chip, _ ->
                toggleChipSelection(chip as Chip)
            }
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

}
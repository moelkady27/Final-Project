package com.example.finalproject.ui.residence_details.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.update_listing.factory.GetResidenceFactory
import com.example.finalproject.ui.update_listing.repository.GetResidenceRepository
import com.example.finalproject.ui.update_listing.viewModel.GetResidenceViewModel
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_building_type_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_electrical_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_neighborhood_update
import kotlinx.android.synthetic.main.activity_first_update.auto_complete_sale_type_update
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
import kotlinx.android.synthetic.main.activity_first_update.utilities_electricity_update
import kotlinx.android.synthetic.main.activity_first_update.utilities_gas_update
import kotlinx.android.synthetic.main.activity_first_update.utilities_water_update
import kotlinx.android.synthetic.main.fragment_description.apartment_location_description
import kotlinx.android.synthetic.main.fragment_description.cv_basement
import kotlinx.android.synthetic.main.fragment_description.cv_fireplace
import kotlinx.android.synthetic.main.fragment_description.cv_garage_cars
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_age
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_bathrooms
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_bedrooms
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_fireplace
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_garage_cars
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_kitchen
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_pool_area
import kotlinx.android.synthetic.main.fragment_description.textView_number_of_total_area
import kotlinx.android.synthetic.main.fragment_description.tv_agent_name
import kotlinx.android.synthetic.main.fragment_description.tv_alley_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_building_type_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_condition_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_condition_details_4
import kotlinx.android.synthetic.main.fragment_description.tv_exterior_condition_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_exterior_covering_on_house_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_exterior_covering_on_house_details_4
import kotlinx.android.synthetic.main.fragment_description.tv_exterior_quality_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_fire_place_quality_details_1
import kotlinx.android.synthetic.main.fragment_description.tv_fire_place_quality_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_foundation_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_garage_quality_details_1
import kotlinx.android.synthetic.main.fragment_description.tv_garage_quality_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_heating_quality_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_house_style_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_kitchen_quality_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_lot_shape_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_masonry_veneer_area_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_masonry_veneer_type_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_month_of_sold_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_msZoning_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_roof_material_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_roof_style_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_sale_condition_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_sale_type_details_2
import kotlinx.android.synthetic.main.fragment_description.tv_street_details_2
import org.json.JSONException
import org.json.JSONObject

class DescriptionFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var getResidenceViewModel: GetResidenceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
                                    /* Get-Residence */

        val getResidenceRepository = GetResidenceRepository(RetrofitClient.instance)
        val factoryGetResidence = GetResidenceFactory(getResidenceRepository)
        getResidenceViewModel = ViewModelProvider(
            this@DescriptionFragment, factoryGetResidence)[GetResidenceViewModel::class.java]

        getResidence()
    }

    private fun getResidence() {
        val token = AppReferences.getToken(requireContext())
        val residenceId = "6668f9b9b1d92be79eac207c"

        Log.e("Residence ID", "ResidenceID: $residenceId")

        baseActivity.showProgressDialog(requireContext(), "please wait...")

        getResidenceViewModel.getResidence(token, residenceId)

        getResidenceViewModel.getResidenceResponseLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response.let {

                val image = response.residence.ownerId.image.url
                val imageView: ImageView = requireView().findViewById(R.id.iv_agent_image)
                Glide.with(this)
                    .load(image)
                    .into(imageView)

                tv_agent_name.text = response.residence.ownerId.username

                apartment_location_description.text = response.residence.location.fullAddress

                textView_number_of_total_area.text = response.residence.totalarea

                textView_number_of_pool_area.text = response.residence.poolArea

                textView_number_of_age.text = response.residence.houseage

                textView_number_of_bedrooms.text = response.residence.bedroomAbvGr.toString()

                textView_number_of_kitchen.text = response.residence.KitchenAbvGr.toString()

                textView_number_of_bathrooms.text = response.residence.totalbaths.toString()

                if (!response.residence.hasBasement){
                    cv_basement.visibility = View.GONE
                }

                if (!response.residence.hasGarage){
                    cv_garage_cars.visibility = View.GONE
                    tv_garage_quality_details_1.visibility = View.GONE
                    tv_garage_quality_details_2.visibility = View.GONE
                }
                else {
                    textView_number_of_garage_cars.text = response.residence.garageCars.toString()
                    tv_garage_quality_details_2.text = response.residence.garageQual
                }

                if (!response.residence.hasFireplace){
                    cv_fireplace.visibility = View.GONE
                    tv_fire_place_quality_details_1.visibility = View.GONE
                    tv_fire_place_quality_details_2.visibility = View.GONE

                } else {
                    textView_number_of_fireplace.text = response.residence.fireplaces.toString()
                    tv_fire_place_quality_details_2.text = response.residence.fireplaceQu
                }

                tv_msZoning_details_2.text = response.residence.mszoning

                tv_sale_condition_details_2.text = response.residence.saleCondition

                tv_month_of_sold_details_2.text = response.residence.moSold

                tv_sale_type_details_2.text = response.residence.saleType

                tv_foundation_details_2.text = response.residence.foundation

                tv_street_details_2.text = response.residence.street

                tv_roof_style_details_2.text = response.residence.roofStyle

                tv_roof_material_details_2.text = response.residence.roofMatl

                tv_building_type_details_2.text = response.residence.bldgType

                tv_lot_shape_details_2.text = response.residence.lotShape

                tv_house_style_details_2.text = response.residence.houseStyle

                tv_alley_details_2.text = response.residence.alley

                tv_masonry_veneer_type_details_2.text = response.residence.masVnrType

                tv_masonry_veneer_area_details_2.text = response.residence.masVnrArea.toString()

                tv_exterior_covering_on_house_details_2.text = response.residence.exterior1st

                tv_exterior_covering_on_house_details_4.text = response.residence.exterior2nd

                tv_condition_details_2.text = response.residence.condition1

                tv_condition_details_4.text = response.residence.condition2

                tv_heating_quality_details_2.text = response.residence.heatingQc

                tv_exterior_quality_details_2.text = response.residence.exterQual

                tv_exterior_condition_details_2.text = response.residence.exterCond

                tv_kitchen_quality_details_2.text = response.residence.kitchenQual
            }

            getResidenceViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
                baseActivity.hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            requireContext(),
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("DescriptionFragment", "Description Fragment Error: $errorMessage")

                    } catch (e: JSONException) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

}
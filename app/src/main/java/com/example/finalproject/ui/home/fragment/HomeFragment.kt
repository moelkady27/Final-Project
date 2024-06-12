package com.example.finalproject.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.activities.CreateResidenceActivity
import com.example.finalproject.ui.chat.activities.ChatListUsersActivity
import com.example.finalproject.ui.favourite.factory.AddToFavouritesFactory
import com.example.finalproject.ui.favourite.factory.DeleteFavouriteFactory
import com.example.finalproject.ui.favourite.repository.AddToFavouritesRepository
import com.example.finalproject.ui.favourite.repository.DeleteFavouriteRepository
import com.example.finalproject.ui.favourite.viewModel.AddToFavouritesViewModel
import com.example.finalproject.ui.favourite.viewModel.DeleteFavouriteViewModel
import com.example.finalproject.ui.home.activities.FeaturedEstatesActivity
import com.example.finalproject.ui.home.activities.PopularNearestYouActivity
import com.example.finalproject.ui.home.activities.SearchResultsActivity
import com.example.finalproject.ui.home.adapter.HomeFeaturedAdapter
import com.example.finalproject.ui.home.adapter.HomePopularAdapter
import com.example.finalproject.ui.home.factory.HomeFeaturedEstatesFactory
import com.example.finalproject.ui.home.factory.HomePopularEstatesFactory
import com.example.finalproject.ui.home.repository.HomeFeaturedEstatesRepository
import com.example.finalproject.ui.home.repository.HomePopularEstatesRepository
import com.example.finalproject.ui.home.viewModel.HomeFeaturedEstatesViewModel
import com.example.finalproject.ui.home.viewModel.HomePopularEstatesViewModel
import com.example.finalproject.ui.profile.factory.GetUserInfoFactory
import com.example.finalproject.ui.profile.repository.GetUserInfoRepository
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import kotlinx.android.synthetic.main.fragment_home.iv_add_list_home
import kotlinx.android.synthetic.main.fragment_home.iv_chat_home
import kotlinx.android.synthetic.main.fragment_home.iv_search_home
import kotlinx.android.synthetic.main.fragment_home.tv_home_title_3
import kotlinx.android.synthetic.main.fragment_home.tv_home_title_5
import kotlinx.android.synthetic.main.fragment_home.tv_location_home_title_2
import kotlinx.android.synthetic.main.fragment_home.tv_search_home
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {
    private lateinit var baseActivity: BaseActivity

    private lateinit var networkUtils: NetworkUtils

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    private lateinit var homePopularEstatesViewModel: HomePopularEstatesViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var homePopularAdapter: HomePopularAdapter

    private lateinit var homeFeaturedAdapter: HomeFeaturedAdapter

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

    private lateinit var addToFavouritesViewModel: AddToFavouritesViewModel

    private lateinit var deleteFavouriteViewModel: DeleteFavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = BaseActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkUtils = NetworkUtils(requireContext())

        initView()

        initButtons()
    }

    private fun initView(){
                                    /* Home Featured Estates */

        val homeFeaturedEstatesRepository = HomeFeaturedEstatesRepository(RetrofitClient.instance)
        val factory = HomeFeaturedEstatesFactory(homeFeaturedEstatesRepository)
        homeFeaturedEstatesViewModel = ViewModelProvider(
            this@HomeFragment, factory)[HomeFeaturedEstatesViewModel::class.java]

        recyclerView = requireView().findViewById(R.id.rv_home_featured)
        recyclerView.layoutManager = LinearLayoutManager(requireContext() ,
            LinearLayoutManager.VERTICAL , false)

        homeFeaturedAdapter = HomeFeaturedAdapter(mutableListOf())
        recyclerView.adapter = homeFeaturedAdapter

        val token = AppReferences.getToken(requireContext())

        homeFeaturedEstatesViewModel.getFeaturedEstates(token)

        homeFeaturedEstatesViewModel.homeFeaturedEstatesLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                homeFeaturedAdapter.setItems(it.residences)
                Log.e("HomeFragment", it.residences.size.toString())
            }
        }


                                    /* Home Popular Estates */

        val homePopularEstatesRepository = HomePopularEstatesRepository(RetrofitClient.instance)
        val popularEstatesFactory = HomePopularEstatesFactory(homePopularEstatesRepository)
        homePopularEstatesViewModel = ViewModelProvider(
            this@HomeFragment, popularEstatesFactory)[HomePopularEstatesViewModel::class.java]

        recyclerView = requireView().findViewById(R.id.rv_home_popular)
        recyclerView.layoutManager = LinearLayoutManager(requireContext() ,
            LinearLayoutManager.HORIZONTAL , false)

        homePopularAdapter = HomePopularAdapter(mutableListOf(), ::handleFavouriteClick)
        recyclerView.adapter = homePopularAdapter

        homePopularEstatesViewModel.getPopularEstates(token)

        homePopularEstatesViewModel.homePopularEstatesLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                homePopularAdapter.setPopularItems(it.residences)
                Log.e("HomePopularEstates", it.residences.size.toString())
            }
        }

                            /* user location */

        val getUserInfoRepository = GetUserInfoRepository(RetrofitClient.instance)
        val factoryGetUser = GetUserInfoFactory(getUserInfoRepository)
        getUserInfoViewModel = ViewModelProvider(
            this@HomeFragment, factoryGetUser)[GetUserInfoViewModel::class.java]

        getUserInfo()
    }

    private fun getUserInfo() {
        getUserInfoViewModel.getUserInfo(AppReferences.getToken(requireContext()))

        getUserInfoViewModel.getUserInfoResponseLiveData.observe(viewLifecycleOwner
        ) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                response.user.let { user ->
                    val location = user.location.fullAddress

                    tv_location_home_title_2.text = location
                }
            }
        }

        getUserInfoViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            baseActivity.hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

                    Log.e("EditProfileActivity", "Error Update: $errorMessage")

                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleFavouriteClick(residenceId: String, isLiked: Boolean) {
        if (!isLiked) {

                                /* Add To Favourites */

            val addToFavouritesRepository = AddToFavouritesRepository(RetrofitClient.instance)
            val addToFavouritesFactory = AddToFavouritesFactory(addToFavouritesRepository)
            addToFavouritesViewModel = ViewModelProvider(
                this@HomeFragment, addToFavouritesFactory
            )[AddToFavouritesViewModel::class.java]

            val token = AppReferences.getToken(requireContext())

            addToFavouritesViewModel.addToFavourites(token, residenceId)

            addToFavouritesViewModel.addToFavouritesLiveData.removeObservers(this)
            addToFavouritesViewModel.addToFavouritesLiveData.observe(
                this@HomeFragment) { response ->
                baseActivity.hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("AddToFavourites", "Added to Favourites $status")
                    homePopularAdapter.updateFavouriteStatus(residenceId, true)
                }
            }

            addToFavouritesViewModel.errorLiveData.removeObservers(this)
            addToFavouritesViewModel.errorLiveData.observe(this@HomeFragment) { error ->
                baseActivity.hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            requireContext(),
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("AddToFavourites", errorMessage)
                    } catch (e: JSONException) {
                        Toast.makeText(
                            requireContext(),
                            error,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.e("AddToFavourites error is", error)
                    }
                }
            }

        } else {
                                    /* Delete Favourite */

            val deleteFavouritesRepository = DeleteFavouriteRepository(RetrofitClient.instance)
            val deleteFavouriteFactory = DeleteFavouriteFactory(deleteFavouritesRepository)
            deleteFavouriteViewModel = ViewModelProvider(
                this@HomeFragment, deleteFavouriteFactory
            )[DeleteFavouriteViewModel::class.java]

            val token = AppReferences.getToken(requireContext())
            deleteFavouriteViewModel.deleteFavourite(token, residenceId)

            deleteFavouriteViewModel.deleteFavouriteLiveData.removeObservers(this)
            deleteFavouriteViewModel.deleteFavouriteLiveData.observe(
                this@HomeFragment) { response ->
                baseActivity.hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("DeleteFavourite", "Deleted from Favourites $status")
                    homePopularAdapter.updateFavouriteStatus(residenceId, false)
                }
            }

            deleteFavouriteViewModel.errorLiveData.removeObservers(this)
            deleteFavouriteViewModel.errorLiveData.observe(this@HomeFragment) { error ->
                baseActivity.hideProgressDialog()
                error.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(
                            requireContext(),
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: JSONException) {
                        Toast.makeText(
                            requireContext(),
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun initButtons(){
        tv_home_title_3.setOnClickListener {
            val intent = Intent(requireContext() , PopularNearestYouActivity::class.java)
            startActivity(intent)
        }

        tv_home_title_5.setOnClickListener {
            val intent = Intent(requireContext() , FeaturedEstatesActivity::class.java)
            startActivity(intent)
        }

        iv_add_list_home.setOnClickListener {
            val intent = Intent(requireContext() , CreateResidenceActivity::class.java)
            startActivity(intent)
        }

        tv_search_home.setOnClickListener {
            val intent = Intent(requireContext(), SearchResultsActivity::class.java)
            startActivity(intent)
        }

        iv_search_home.setOnClickListener {
            val intent = Intent(requireContext(), SearchResultsActivity::class.java)
            startActivity(intent)
        }

        iv_chat_home.setOnClickListener {
            startActivity(Intent(requireContext(), ChatListUsersActivity::class.java))
        }
    }
}

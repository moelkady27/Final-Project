package com.example.finalproject.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.add_listing.activities.CreateResidenceActivity
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
import kotlinx.android.synthetic.main.fragment_home.iv_add_list_home
import kotlinx.android.synthetic.main.fragment_home.iv_search_home
import kotlinx.android.synthetic.main.fragment_home.tv_home_title_3
import kotlinx.android.synthetic.main.fragment_home.tv_home_title_5
import kotlinx.android.synthetic.main.fragment_home.tv_search_home

class HomeFragment : Fragment() {
    private lateinit var baseActivity: BaseActivity

    private lateinit var networkUtils: NetworkUtils

    private lateinit var homeFeaturedEstatesViewModel: HomeFeaturedEstatesViewModel

    private lateinit var homePopularEstatesViewModel: HomePopularEstatesViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var homePopularAdapter: HomePopularAdapter

    private lateinit var homeFeaturedAdapter: HomeFeaturedAdapter

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

        homePopularAdapter = HomePopularAdapter(mutableListOf())
        recyclerView.adapter = homePopularAdapter

        homePopularEstatesViewModel.getPopularEstates(token)

        homePopularEstatesViewModel.homePopularEstatesLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                homePopularAdapter.setPopularItems(it.residences)
                Log.e("HomePopularEstates", it.residences.size.toString())
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
    }
}

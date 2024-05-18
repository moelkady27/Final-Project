package com.example.finalproject.ui.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.profile.activities.EditProfileActivity
import com.example.finalproject.ui.profile.adapter.MyViewAdapter
import com.example.finalproject.ui.profile.db.UserDatabase
import com.example.finalproject.ui.profile.factory.GetUserInfoFactory
import com.example.finalproject.ui.profile.models.User
import com.example.finalproject.ui.profile.repository.GetUserInfoRepository
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_profile.floatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.image_profile
import kotlinx.android.synthetic.main.fragment_profile.tab_layout_profile
import kotlinx.android.synthetic.main.fragment_profile.tv_profile_email
import kotlinx.android.synthetic.main.fragment_profile.tv_profile_name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: MyViewAdapter

    private var isProfileUpdated = false

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
            R.layout.fragment_profile,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2 = view.findViewById(R.id.view_pager_profile)

        adapter = MyViewAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tab_layout_profile, viewPager2) { tab, position ->
            when (position) {
                0 ->
                    tab.text = "Pending"
                1 ->
                    tab.text = "Listings"
                2 ->
                    tab.text = "Sold"
            }
        }.attach()

        initView()
    }

    private fun initView(){

        val userId = AppReferences.getUserId(requireContext())

        /* Get-User-Info-Profile */

        val getUserInfoRepository = GetUserInfoRepository(RetrofitClient.instance)
        val factoryGetUserProfile = GetUserInfoFactory(getUserInfoRepository)
        getUserInfoViewModel = ViewModelProvider(
            this@ProfileFragment, factoryGetUserProfile)[GetUserInfoViewModel::class.java]

        getUserInfoViewModel.getUserInfo(AppReferences.getToken(requireContext()))

        getUserInfoViewModel.getUserInfoResponseLiveData.observe(viewLifecycleOwner) { response ->
            BaseActivity().hideProgressDialog()
            response.let {
                val status = response.status

                Log.e("GetUser", status)

                val user = response.user
                val userEntity = User(
                    _id = user._id,
                    createdAt = user.createdAt,
                    email = user.email,
                    firstName = user.firstName,
                    fullName = user.fullName,
                    gender = user.gender,
                    image = user.image,
                    isVerified = user.isVerified,
                    lastName = user.lastName,
                    location = user.location,
                    phone = user.phone,
                    role = user.role,
                    updatedAt = user.updatedAt,
                    username = user.username
                )

                lifecycleScope.launch {
                    val userDao = UserDatabase.getInstance(requireContext()).userDao()
                    userDao.saveUser(userEntity)

                    val updatedUser = userDao.getUser(userEntity._id)
                    updateProfile(updatedUser)

                    if (!isProfileUpdated) {
                        isProfileUpdated = true
                        getUserInfoViewModel.getUserInfo(AppReferences.getToken(requireContext()))
                    }
                }
            }
        }

        getUserInfoViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext() , EditProfileActivity::class.java)
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val user = UserDatabase.getInstance(requireContext()).userDao().getUser(userId)

            withContext(Dispatchers.Main) {

                val fullName = user.fullName
                val email = user.email
                val photo = user.image.url

                tv_profile_name.text = fullName
                tv_profile_email.text = email

                if (photo.isNotEmpty()) {
                    Glide
                        .with(requireActivity())
                        .load(photo)
                        .into(image_profile)
                }
            }
        }

     }

    private fun updateProfile(user: User) {
        user.let {
            tv_profile_name.text = user.fullName
            tv_profile_email.text = user.email

            val photo = user.image.url

            if (photo.isNotEmpty()) {
                Glide.with(requireActivity())
                    .load(photo)
                    .into(image_profile)
            }
        }
    }


}
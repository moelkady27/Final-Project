package com.example.finalproject.ui.setting.fragment

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
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.activities.ChatListUsersActivity
import com.example.finalproject.ui.notification.activity.NotificationActivity
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.password.activities.ChangePasswordActivity
import com.example.finalproject.ui.setting.activities.DeleteAccountActivity
import com.example.finalproject.ui.profile.activities.EditProfileActivity
import com.example.finalproject.ui.profile.db.UserDatabase
import com.example.finalproject.ui.profile.factory.GetUserInfoFactory
import com.example.finalproject.ui.profile.models.User
import com.example.finalproject.ui.profile.repository.GetUserInfoRepository
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import com.example.finalproject.ui.search.SearchUsersActivity
import com.example.finalproject.ui.favourite.activities.FavouritesActivity
import com.example.finalproject.ui.setting.factory.LogOutFactory
import com.example.finalproject.ui.setting.repository.LogOutRepository
import com.example.finalproject.ui.setting.viewModels.LogOutViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_settings.btn_change_password
import kotlinx.android.synthetic.main.fragment_settings.btn_chats
import kotlinx.android.synthetic.main.fragment_settings.btn_delete_account
import kotlinx.android.synthetic.main.fragment_settings.btn_edit_profile
import kotlinx.android.synthetic.main.fragment_settings.btn_favourites
import kotlinx.android.synthetic.main.fragment_settings.btn_logout
import kotlinx.android.synthetic.main.fragment_settings.btn_notification
import kotlinx.android.synthetic.main.fragment_settings.iv_user_settings
import kotlinx.android.synthetic.main.fragment_settings.search_for_user
import kotlinx.android.synthetic.main.fragment_settings.tv_email_name
import kotlinx.android.synthetic.main.fragment_settings.tv_user_name
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class SettingsFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var networkUtils: NetworkUtils

    private lateinit var logOutViewModel: LogOutViewModel

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

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
            R.layout.fragment_settings,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkUtils = NetworkUtils(requireContext())

        initView()
    }

    private fun initView(){
                                        /* Logout */

        val logOutRepository = LogOutRepository(RetrofitClient.instance)
        val factory = LogOutFactory(logOutRepository)
        logOutViewModel = ViewModelProvider(
            this@SettingsFragment, factory)[LogOutViewModel::class.java]

        btn_logout.setOnClickListener {
            btn_logout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_logout.setTextColor(resources.getColor(R.color.white))
            btn_logout.setIconTintResource(R.color.white)

            resetButtonColor(btn_logout)

            if (networkUtils.isNetworkAvailable()) {
                baseActivity.showProgressDialog(requireContext(), "Logging Out...")
                logOutViewModel.logout(AppReferences.getToken(requireContext()))

                viewModelResponse()
            } else {
                baseActivity.showErrorSnackBar("No internet connection", true)
            }
        }

                                        /* Get-User-Info */

//        if (networkUtils.isNetworkAvailable()) {
//            fetchUserDataFromServer()
//        } else {
//            fetchUserDataFromCache()
//        }

        fetchUserDataFromCache()

        if (networkUtils.isNetworkAvailable()) {
            fetchUserDataFromServer()
        }

        additionalButtons()
    }

    private fun fetchUserDataFromServer() {
        val getUserInfoRepository = GetUserInfoRepository(RetrofitClient.instance)
        val factoryGetUser = GetUserInfoFactory(getUserInfoRepository)
        getUserInfoViewModel = ViewModelProvider(
            this@SettingsFragment, factoryGetUser
        )[GetUserInfoViewModel::class.java]

        getUserInfoViewModel.getUserInfo(AppReferences.getToken(requireContext()))

        getUserInfoViewModel.getUserInfoResponseLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
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
                    username = user.username,
                    soldCount = response.soldCount,
                    pendingCount = response.pendingCount,
                    approvedCount = response.approvedCount
                )

                lifecycleScope.launch {
                    val userDao = UserDatabase.getInstance(requireContext()).userDao()
                    userDao.saveUser(userEntity)

                    updateProfile(userEntity)
                }
            }
        }

        getUserInfoViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            baseActivity.hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    // Handle JSON exception
                }
            }
        }
    }

    private fun fetchUserDataFromCache() {
        lifecycleScope.launch {
            val userDao = UserDatabase.getInstance(requireContext()).userDao()
            val cachedUser = userDao.getUser(AppReferences.getUserId(requireContext()))

            if (cachedUser != null) {
                updateProfile(cachedUser)
            } else {
                if (networkUtils.isNetworkAvailable()) {
                    fetchUserDataFromServer()
                } else {
                    Toast.makeText(requireContext(), "No user data available", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateProfile(user: User?) {
        user?.let {
            tv_user_name.text = it.fullName
            tv_email_name.text = it.email

            val photo = it.image.url

            if (photo.isNotEmpty()) {
                Glide.with(requireActivity())
                    .load(photo)
                    .into(iv_user_settings)
            }

            lifecycleScope.launch {
                val userDao = UserDatabase.getInstance(requireContext()).userDao()
                userDao.saveUser(it)
            }
        } ?: run {
            Toast.makeText(requireContext(), "User data is not available", Toast.LENGTH_LONG).show()
        }
    }

    private fun viewModelResponse() {
        logOutViewModel.logOutResponseLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.status
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

                Log.e("SignUpActivity", "Response Message: $message")

                AppReferences.setLoginState(requireActivity(), false)
                startActivity(Intent(requireContext(), SignInActivity::class.java))

                activity?.finish()
            }
        }

        logOutViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            baseActivity.hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun resetButtonColor(button: MaterialButton) {
        button.postDelayed({
            button.setBackgroundColor(resources.getColor(R.color.white))
            button.setTextColor(resources.getColor(R.color.colorPrimaryText))
            button.setIconTintResource(R.color.colorPrimaryText)
        }, 100)
    }

    private fun additionalButtons() {
        btn_edit_profile.setOnClickListener {
            btn_edit_profile.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_edit_profile.setTextColor(resources.getColor(R.color.white))
            btn_edit_profile.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_edit_profile)
        }

        btn_delete_account.setOnClickListener {
            btn_delete_account.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_delete_account.setTextColor(resources.getColor(R.color.white))
            btn_delete_account.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), DeleteAccountActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_delete_account)
        }

        btn_change_password.setOnClickListener {
            btn_change_password.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_change_password.setTextColor(resources.getColor(R.color.white))
            btn_change_password.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_change_password)
        }

        btn_notification.setOnClickListener {
            btn_notification.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_notification.setTextColor(resources.getColor(R.color.white))
            btn_notification.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_notification)
        }

        btn_chats.setOnClickListener {
            btn_chats.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_chats.setTextColor(resources.getColor(R.color.white))
            btn_chats.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), ChatListUsersActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_chats)
        }

        btn_favourites.setOnClickListener {
            btn_favourites.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_favourites.setTextColor(resources.getColor(R.color.white))
            btn_favourites.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), FavouritesActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_favourites)
        }

        search_for_user.setOnClickListener {
            val intent = Intent(requireContext(), SearchUsersActivity::class.java)
            startActivity(intent)
            resetButtonColor(btn_chats)
        }
    }

}
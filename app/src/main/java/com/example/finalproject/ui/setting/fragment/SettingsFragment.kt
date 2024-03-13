package com.example.finalproject.ui.setting.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.network.NetworkUtils
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.chat.activities.ChatListUsersActivity
import com.example.finalproject.ui.notification.activity.NotificationActivity
import com.example.finalproject.ui.register.activities.SignInActivity
import com.example.finalproject.ui.password.activities.ChangePasswordActivity
import com.example.finalproject.ui.setting.activities.DeleteAccountActivity
import com.example.finalproject.ui.profile.activities.EditProfileActivity
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import com.example.finalproject.ui.setting.viewModels.LogOutViewModels
import kotlinx.android.synthetic.main.fragment_settings.btn_change_password
import kotlinx.android.synthetic.main.fragment_settings.btn_chats
import kotlinx.android.synthetic.main.fragment_settings.btn_delete_account
import kotlinx.android.synthetic.main.fragment_settings.btn_edit_profile
import kotlinx.android.synthetic.main.fragment_settings.btn_logout
import kotlinx.android.synthetic.main.fragment_settings.btn_notification
import kotlinx.android.synthetic.main.fragment_settings.iv_user_settings
import kotlinx.android.synthetic.main.fragment_settings.tv_email_name
import kotlinx.android.synthetic.main.fragment_settings.tv_user_name
import org.json.JSONException
import org.json.JSONObject

class SettingsFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var networkUtils: NetworkUtils

    private lateinit var logOutViewModel: LogOutViewModels

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

//        logout
        logOutViewModel = ViewModelProvider(this@SettingsFragment).get(LogOutViewModels::class.java)
        getUserInfoViewModel =ViewModelProvider(this@SettingsFragment).get(GetUserInfoViewModel::class.java)

        logOutViewModel.logOutResponseLiveData.observe(requireActivity(), Observer { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.status
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

                Log.e("SignUpActivity", "Response Message: $message")

                AppReferences.setLoginState(requireActivity() , false)
                startActivity(Intent(requireContext(), SignInActivity::class.java))

                activity?.finish()
            }
        })

        logOutViewModel.errorLiveData.observe(requireActivity(), Observer { error ->
            baseActivity.hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        })

//        Get-User


        getUserInfoViewModel.getUserInfoResponseLiveData.observe(requireActivity(), Observer { response ->
            baseActivity.hideProgressDialog()
            response.let {
                val status = response.status

                Log.e("GetUser", status)

                val fullName = response.user.fullName
                val email = response.user.email
                val photo = response.user.image.url

                tv_user_name.text = fullName
                tv_email_name.text = email

                Glide
                    .with(requireActivity())
                    .load(photo)
                    .into(iv_user_settings)

            }
        })

        getUserInfoViewModel.errorLiveData.observe(requireActivity(), Observer { error ->
            baseActivity.hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        })

        getUserInfoViewModel.getUserInfo(AppReferences.getToken(requireContext()))

        btn_edit_profile.setOnClickListener {
            btn_edit_profile.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_edit_profile.setTextColor(resources.getColor(R.color.white))
            btn_edit_profile.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        btn_delete_account.setOnClickListener {
            btn_delete_account.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_delete_account.setTextColor(resources.getColor(R.color.white))
            btn_delete_account.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), DeleteAccountActivity::class.java)
            startActivity(intent)
        }

        btn_change_password.setOnClickListener {
            btn_change_password.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_change_password.setTextColor(resources.getColor(R.color.white))
            btn_change_password.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            btn_logout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_logout.setTextColor(resources.getColor(R.color.white))
            btn_logout.setIconTintResource(R.color.white)

            if (networkUtils.isNetworkAvailable()) {
                baseActivity.showProgressDialog(requireContext(), "Logging Out...")
                logOutViewModel.logout(AppReferences.getToken(requireContext()))
            } else {
                baseActivity.showErrorSnackBar("No internet connection", true)
            }
        }

        btn_notification.setOnClickListener {
            btn_notification.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_notification.setTextColor(resources.getColor(R.color.white))
            btn_notification.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        btn_chats.setOnClickListener {
            btn_chats.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_chats.setTextColor(resources.getColor(R.color.white))
            btn_chats.setIconTintResource(R.color.white)

            val intent = Intent(requireContext(), ChatListUsersActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.example.finalproject.ui.profile.fragment

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
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.profile.activities.EditProfileActivity
import com.example.finalproject.ui.profile.viewModels.GetUserInfoViewModel
import kotlinx.android.synthetic.main.fragment_profile.floatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.image_profile
import kotlinx.android.synthetic.main.fragment_profile.tv_profile_email
import kotlinx.android.synthetic.main.fragment_profile.tv_profile_name
import kotlinx.android.synthetic.main.fragment_settings.iv_user_settings
import kotlinx.android.synthetic.main.fragment_settings.tv_email_name
import kotlinx.android.synthetic.main.fragment_settings.tv_user_name
import org.json.JSONException
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private lateinit var getUserInfoViewModel: GetUserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


        getUserInfoViewModel = ViewModelProvider(this@ProfileFragment).get(GetUserInfoViewModel::class.java)

        getUserInfoViewModel.getUserInfoResponseLiveData.observe(requireActivity(), Observer { response ->
            BaseActivity().hideProgressDialog()
            response.let {
                val status = response.status

                Log.e("GetUser", status)

                val fullName = response.user.fullName
                val email = response.user.email
                val photo = response.user.image.url

                tv_profile_name.text = fullName
                tv_profile_email.text = email

                Glide
                    .with(requireActivity())
                    .load(photo)
                    .into(image_profile)

            }
        })

        getUserInfoViewModel.errorLiveData.observe(requireActivity(), Observer { error ->
            BaseActivity().hideProgressDialog()
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

        floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext() , EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
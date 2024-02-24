package com.example.finalproject.ui.setting.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.R
import com.example.finalproject.ui.setting.activities.ChangePasswordActivity
import com.example.finalproject.ui.setting.activities.DeleteAccountActivity
import kotlinx.android.synthetic.main.fragment_settings.btn_change_password
import kotlinx.android.synthetic.main.fragment_settings.btn_delete_account
import kotlinx.android.synthetic.main.fragment_settings.btn_edit_profile

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        btn_edit_profile.setOnClickListener {
            btn_edit_profile.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_edit_profile.setTextColor(resources.getColor(R.color.white))
            btn_edit_profile.setIconTintResource(R.color.white)
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
    }
}
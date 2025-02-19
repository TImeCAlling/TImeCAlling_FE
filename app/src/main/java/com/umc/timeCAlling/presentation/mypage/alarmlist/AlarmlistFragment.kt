package com.umc.timeCAlling.presentation.mypage.alarmlist

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageAlarmlistBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmlistFragment : BaseFragment<FragmentMypageAlarmlistBinding>(R.layout.fragment_mypage_alarmlist) {
    private lateinit var navController: NavController

    override fun initView() {
        bottomNavigationRemove()
        initSetOnClickListener()
    }

    override fun initObserver() {
    }

    private fun initSetOnClickListener() {
        binding.apply {
            ivAlarmlistBack.setOnClickListener { findNavController().navigate(R.id.action_alarmlistFragment_to_mypageFragment) }
            ivAlarmlistAdd.setOnClickListener { findNavController().navigate(R.id.action_alarmlistFragment_to_addAlarmFragment)  }
            switchAlarmlistWakeup.setOnCheckedChangeListener { _, isChecked -> setSwitchColor(binding.switchAlarmlistWakeup, isChecked)}
            switchAlarmlist5min.setOnCheckedChangeListener { _, isChecked -> setSwitchColor(binding.switchAlarmlist5min, isChecked)}
            switchAlarmlist10min.setOnCheckedChangeListener { _, isChecked -> setSwitchColor(binding.switchAlarmlist10min, isChecked)}
            switchAlarmlistDeparture.setOnCheckedChangeListener { _, isChecked -> setSwitchColor(binding.switchAlarmlistDeparture, isChecked)}

        }
    }

    private fun setSwitchColor(switch: MaterialSwitch, isChecked: Boolean) {
        if(isChecked) {
            switch.trackTintList = getResources().getColorStateList(R.color.mint_main)
        } else {
            switch.trackTintList = getResources().getColorStateList(R.color.gray_400)
        }
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }
}
package com.umc.timeCAlling.presentation.mypage

import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment: BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    override fun initView() {
        setClickListener()
        bottomNavigationShow()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.apply {
            clMypageSetting.setOnClickListener {
                navigateToMyprofileFragment() // 내 프로필
            }
            layoutMypageAlarmlist.setOnClickListener {
                //알람리스트
            }
            layoutMypageCategory.setOnClickListener {
                //카테고리
            }
            layoutMypageSoundInfo.setOnClickListener {
                //음성정보
            }
            layoutMypageTerms.setOnClickListener {
                //이용약관
            }
        }
    }

    private fun bottomNavigationShow() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
    }

    private fun navigateToMyprofileFragment() {
        findNavController().navigate(R.id.action_mypageFragment_to_myprofileFragment)
    }
}
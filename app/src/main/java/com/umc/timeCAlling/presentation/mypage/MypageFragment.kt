package com.umc.timeCAlling.presentation.mypage

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageBinding
import com.umc.timeCAlling.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment: BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun initView() {
        setClickListener()
        bottomNavigationShow()
        initSuccessRate()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.apply {
            clMypageSetting.setOnClickListener {
                navigateToMyprofileFragment() // 내 프로필
            }
            layoutMypageAlarmlist.setOnClickListener {
                findNavController().navigate(R.id.action_mypageFragment_to_alarmlistFragment)
            }
            layoutMypageCategory.setOnClickListener {
                findNavController().navigate(R.id.action_mypageFragment_to_categoryEditFragment)
            }
            layoutMypageSoundInfo.setOnClickListener {
                navigateToMypageVoiceFragment()
            }
            layoutMypageTerms.setOnClickListener {
                //이용약관
            }
            ivMypageBack.setOnClickListener { findNavController().navigate(R.id.action_global_homeFragment) }
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

    private fun navigateToMypageVoiceFragment() {
        findNavController().navigate(R.id.action_mypageFragment_to_mypageVoiceFragment)
    }

    private fun initSuccessRate() {
        binding.viewMypageProgressBackground.post {
            val maxWidth = binding.viewMypageProgressBackground.width
            homeViewModel.getSuccessRate()
            homeViewModel.successRate.observe(viewLifecycleOwner) { response ->
                binding.apply {
                    tvMypageCurrentProgress.text = "약속 성공률 ${response.successRate}%\n다음 목표 ${if(response.successRate + 10 > 100) 100 else response.successRate + 10}%, 함께 도전해요!"
                    tvMypageProgressPercent.text = "${response.successRate}%"
                    viewMypageProgressForeground.layoutParams = (viewMypageProgressForeground.layoutParams as ViewGroup.LayoutParams).apply {
                        this.width = maxWidth * response.successRate / 100
                        if(this.width < requireContext().toPx(20).toInt()) this.width = requireContext().toPx(20).toInt()
                    }
                    tvMypageScheduleSuccess.text = "${response.success}개"
                    tvMypageScheduleFailure.text = "${response.failed}개"
                }
            }
        }
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

}
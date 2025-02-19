package com.umc.timeCAlling.presentation.addSchedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.common.KakaoSdk.type
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleSuccessBinding
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.databinding.FragmentCategoryEditBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleSuccessFragment: BaseFragment<FragmentAddScheduleSuccessBinding>(R.layout.fragment_add_schedule_success) {

    private val viewModel: AddScheduleViewModel by activityViewModels()
    private var scheduleId: Int = -1

    override fun initView() {

        if (scheduleId != -1) {
            binding.tvAddScheduleSuccessText.text = "일정이 수정되었습니다."
        } else {
            binding.tvAddScheduleSuccessText.text = "일정이 추가되었습니다."
        }

        bottomNavigationRemove()
        moveToHomeFragment()
        binding.tvAddScheduleSuccessShare.setOnSingleClickListener {
            issueLink()
        }
        binding.ivAddScheduleSuccessBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }

    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        // + 버튼 숨기기
        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun moveToHomeFragment() {
        binding.tvAddScheduleSuccessNext.setOnClickListener {
            viewModel.resetData()
            findNavController().navigate(R.id.action_addScheduleSuccessFragment_to_homeFragment)
            scheduleId = -1
        }
    }

    private fun issueLink() {
        scheduleId = viewModel.scheduleId.value ?: -1
        val deepLink = "https://umc.timecalling.com/schedules/$scheduleId"
        val shareText = "우리 일정 함께할래? 타임콜링으로 지각 걱정 없이 딱 맞춰 가자!\n$deepLink"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "공유하기"))
    }
}
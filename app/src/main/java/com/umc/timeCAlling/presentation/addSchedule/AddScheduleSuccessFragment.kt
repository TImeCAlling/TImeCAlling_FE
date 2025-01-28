package com.umc.timeCAlling.presentation.addSchedule

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleSuccessBinding
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.databinding.FragmentCategoryEditBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleSuccessFragment: BaseFragment<FragmentAddScheduleSuccessBinding>(R.layout.fragment_add_schedule_success) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화

    override fun initView() {

        bottomNavigationRemove()
        moveToHomeFragment()
        issueLink()

        binding.ivAddScheduleSuccessBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }

    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
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
            findNavController().navigate(R.id.action_addScheduleSuccessFragment_to_homeFragment)
        }
    }

    private fun issueLink() {
        binding.tvAddScheduleSuccessShare.setOnClickListener {
            val scheduleId = 1
            // EC2 인스턴스의 Public IP와 포트 번호를 포함
            val deepLink = Uri.parse("http://50.19.156.109:3000/schedules/$scheduleId")
            shareDeepLink(deepLink)
        }
    }
    private fun shareDeepLink(deepLink: Uri) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("deep_link", deepLink.toString())
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, deepLink.toString())
        }
        startActivity(Intent.createChooser(shareIntent, "일정 공유"))
    }
}
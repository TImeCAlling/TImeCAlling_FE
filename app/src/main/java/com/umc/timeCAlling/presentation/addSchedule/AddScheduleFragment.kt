package com.umc.timeCAlling.presentation.addSchedule

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.dialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment: BaseFragment<FragmentAddScheduleBinding>(R.layout.fragment_add_schedule) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun initView() {

        bottomNavigationRemove()
        initDateBottomSheet()

    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE
    }

    private fun initDateBottomSheet() {

        val calendarView = binding.calendarView // MaterialCalendarView
        val dateTextView = binding.tvAddScheduleDate // 날짜를 표시할 TextView

        val bottomSheet = binding.bottomSheet // BottomSheet 레이아웃 ID
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김

        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        bottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                val selectedDate = "${date.year}년 ${date.month + 1}월 ${date.day}일" // 선택한 날짜 형식 지정
                dateTextView.text = selectedDate // TextView에 날짜 설정

                // BottomSheet 숨기기
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        // 이미지 클릭 리스너
        binding.layoutAddScheduleDate.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }
}


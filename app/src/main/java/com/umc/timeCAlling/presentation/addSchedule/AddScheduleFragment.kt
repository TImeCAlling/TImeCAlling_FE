package com.umc.timeCAlling.presentation.addSchedule

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment: BaseFragment<FragmentCalendarBinding>(R.layout.fragment_add_schedule) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomNavigationView 찾기 (Activity의 레이아웃에 있는 경우)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)

        // BottomNavigationView 숨기기
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BottomNavigationView 다시 표시 (다른 Fragment로 이동할 때)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun initView() {

    }

    override fun initObserver() {

    }
}
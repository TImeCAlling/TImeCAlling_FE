package com.umc.timeCAlling.presentation.calendar

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.presentation.home.LastSchedule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment: BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private lateinit var navController: NavController
    override fun initView() {
        initDetailScheduleRV()
    }

    override fun initObserver() {

    }

    private fun initDetailScheduleRV() {
        //test data
        var list = arrayListOf<DetailSchedule>(
            DetailSchedule("컴퓨터 구조", "매주 수요일 반복", "일상", true, "9:00", 5),
            DetailSchedule("컴퓨터 구조2", "매주 수요일 반복", "공부", true, "11:00", 4),
            DetailSchedule("컴퓨터 구조3", "매주 수요일 반복", "일상", false, "1:00", 3),
            DetailSchedule("컴퓨터 구조4", "매주 수요일 반복", "공부", false, "2:00", 2)
        )
        val adapter = DetailScheduleRVA(list)
        binding.rvCalendarDetailSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }
}
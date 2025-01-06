package com.umc.timeCAlling.presentation.scheduleList

import androidx.navigation.NavController
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentScheduleListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleListFragment: BaseFragment<FragmentScheduleListBinding>(R.layout.fragment_schedule_list) {
    private lateinit var navController: NavController
    override fun initView() {

    }

    override fun initObserver() {

    }
}
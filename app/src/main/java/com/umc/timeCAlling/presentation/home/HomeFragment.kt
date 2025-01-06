package com.umc.timeCAlling.presentation.home

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var navController: NavController
    override fun initView() {
        binding.tvHomeAddSchedule.setOnSingleClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addScheduleTab)
        }
        binding.layoutHomeTodayScheduleDetail.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_scheduleListFragment)
        }

        initLastScheduleRV()
    }

    override fun initObserver() {

    }

    fun initLastScheduleRV() {
        //test data
        val list = listOf(
            LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
            LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
            LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
            LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
            LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
            LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
        )

        val adapter = LastScheduleAdapter(list)
        binding.rvHomeLastSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }

        if(list.isEmpty()) {
            binding.rvHomeLastSchedule.visibility = View.GONE
            binding.tvHomeNoLastSchedule.visibility = View.VISIBLE
        }
        else {
            binding.rvHomeLastSchedule.visibility = View.VISIBLE
            binding.tvHomeNoLastSchedule.visibility = View.GONE
        }
    }
}

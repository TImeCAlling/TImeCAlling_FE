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
        initTodayScheduleRV()
    }
    override fun initObserver() {

    }

    //test data
    var list = arrayListOf<LastSchedule>(
        LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
        LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
        LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
        LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
        LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
        LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
    )

    private fun initLastScheduleRV() {
        val adapter = LastScheduleAdapter(list,
            onClickDeleteItem = {deleteTask(it)})
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

    fun deleteTask(schedule: LastSchedule) {
        list.remove(schedule)
        binding.rvHomeLastSchedule.adapter?.notifyDataSetChanged()
        if(list.isEmpty()) {
            binding.rvHomeLastSchedule.visibility = View.GONE
            binding.tvHomeNoLastSchedule.visibility = View.VISIBLE
        }
    }

    private fun initTodayScheduleRV() {
        val list2 = arrayListOf<TodaySchedule>(
            TodaySchedule("컴퓨터 구조", "일정 설명", true, "9:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", true, "10:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", false, "12:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", false, "13:00", "24\nmin"),
        )
        val adapter = TodayScheduleAdapter(list2)
        binding.rvHomeTodaySchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        if(list2.isEmpty()) {
            binding.rvHomeTodaySchedule.visibility = View.GONE
            binding.layoutHomeNoTodaySchedule.visibility = View.VISIBLE
        }
        else {
            binding.rvHomeTodaySchedule.visibility = View.VISIBLE
            binding.layoutHomeNoTodaySchedule.visibility = View.GONE
        }
        adapter.deleteClick = object : TodayScheduleAdapter.DeleteClick {
            override fun onClick(view: View, position: Int) {
                list2.removeAt(position)
                adapter.notifyDataSetChanged()
                if(list2.isEmpty()) {
                    binding.rvHomeTodaySchedule.visibility = View.GONE
                    binding.layoutHomeNoTodaySchedule.visibility = View.VISIBLE
                }
            }
        }
    }

}

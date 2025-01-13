package com.umc.timeCAlling.presentation.home

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.presentation.home.adapter.LastScheduleRVA
import com.umc.timeCAlling.presentation.home.adapter.TodayScheduleRVA
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var navController: NavController
    override fun initView() {
        binding.layoutHomeTodayScheduleDetail.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
        binding.ivHomeMypage.setOnSingleClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }

        setProgressBar(6, 6)  //나중에 하기
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
    val listSize = list.size

    private fun initLastScheduleRV() {
        val adapter = LastScheduleRVA(list,
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
        setProgressBar(listSize ,list.size)
    }

    private fun initTodayScheduleRV() {
        val list2 = arrayListOf<TodaySchedule>(
            TodaySchedule("컴퓨터 구조", "일정 설명", true, "9:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", true, "10:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", false, "12:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", false, "13:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", true, "9:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", true, "10:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", false, "12:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", false, "13:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", true, "9:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", true, "10:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", false, "12:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", false, "13:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", true, "9:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", true, "10:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조", "일정 설명", false, "12:00", "24\nmin"),
            TodaySchedule("컴퓨터 구조2", "일정 설명2", false, "13:00", "24\nmin"),
        )
        val listSize = list2.size
        val adapter = TodayScheduleRVA(list2)
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
        adapter.deleteClick = object : TodayScheduleRVA.DeleteClick {
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

    private fun setProgressBar(size: Int, currentSize: Int) {
        val maxWidth = binding.viewHomeProgressBarBackground.width
        val progress = ((1-(currentSize.toFloat() / size.toFloat())) * maxWidth).toInt()
        binding.viewHomeProgressBarForeground.layoutParams = (binding.viewHomeProgressBarForeground.layoutParams as ViewGroup.LayoutParams).apply {
            width = if(progress <= 20) requireContext().toPx(20).toInt() else progress
        }
        binding.tvHomeProgress.text = "${((1 - (currentSize.toFloat() / size.toFloat())) * 100).toInt()}%"
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

}

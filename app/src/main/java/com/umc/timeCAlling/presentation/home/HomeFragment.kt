package com.umc.timeCAlling.presentation.home

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.TopSheetBehavior
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.home.adapter.LastScheduleRVA
import com.umc.timeCAlling.presentation.home.adapter.TodayScheduleRVA
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var navController: NavController
    private lateinit var behavior: TopSheetBehavior<View>

    override fun initView() {
        binding.layoutHomeTodayScheduleDetail.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
        binding.ivHomeMypage.setOnSingleClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mypageTab)
        }
        initLastScheduleRV()
        initTodayScheduleRV()
        initTopSheet()
    }
    override fun initObserver() {

    }

    private fun initTopSheet() {
        behavior = TopSheetBehavior.from(binding.layoutHomeTopSheet)
        behavior.apply {
            this.state = TopSheetBehavior.STATE_HIDDEN
        }
        behavior.setTopSheetCallback(object: TopSheetBehavior.TopSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    TopSheetBehavior.STATE_HIDDEN -> {
                        dismissTopSheet()
                    }
                    TopSheetBehavior.STATE_COLLAPSED -> {
                        dismissTopSheet()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("slideOffset", slideOffset.toString())
            }
        })
        binding.ivPreClose.setOnClickListener {
            behavior.state = TopSheetBehavior.STATE_COLLAPSED
        }
        binding.viewHomeTopsheetBackground.setOnClickListener {
            behavior.state = TopSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun showTopSheet() {
        behavior.state = TopSheetBehavior.STATE_EXPANDED
        binding.viewHomeTopsheetBackground.visibility = View.VISIBLE
        binding.viewHomeTopsheetBackground.setOnSingleClickListener {
            behavior.state = TopSheetBehavior.STATE_COLLAPSED
            binding.viewHomeTopsheetBackground.visibility = View.GONE
        }
    }

    private fun dismissTopSheet() {
        binding.apply {
            viewHomeTopsheetBackground.visibility = View.GONE
            layoutTopsheetScroll.scrollTo(0,0)
        }
    }

    private fun initLastScheduleRV() {
        var list = arrayListOf<LastSchedule>(
            LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
            LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00"),
            LastSchedule("컴퓨터 구조", "일정 설명", true, "9:00"),
            LastSchedule("컴퓨터 구조2", "일정 설명", true, "10:00")
        )
        val listSize = list.size
        setProgressBar(listSize, listSize)
        val adapter = LastScheduleRVA(list)
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
        adapter.itemClick = object : LastScheduleRVA.ItemClick {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(requireContext(), "${list[position].title} Clicked", Toast.LENGTH_SHORT).show()
                val action = HomeFragmentDirections.actionHomeFragmentToCheckListFragment(scheduleIndex =position)
                findNavController().navigate(action)
            }
        }

        val args = HomeFragmentArgs.fromBundle(requireArguments())
        val idx = args.scheduleIndex
        if(idx != -1) {
            list.removeAt(idx)
            adapter.notifyDataSetChanged()
            setProgressBar(listSize, list.size)
            if(list.isEmpty()) {
                binding.rvHomeLastSchedule.visibility = View.GONE
                binding.tvHomeNoLastSchedule.visibility = View.VISIBLE
            }
        }
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
        adapter.itemClick = object : TodayScheduleRVA.ItemClick {
            override fun onClick(view: View, position: Int) {
                showTopSheet()
            }
        }
    }

    private fun setProgressBar(size: Int, currentSize: Int) {
        val maxWidth = binding.viewHomeProgressBarBackground.width
        val progress = ((1-(currentSize.toFloat() / size.toFloat())) * maxWidth).toInt()
        binding.viewHomeProgressBarForeground.layoutParams = (binding.viewHomeProgressBarForeground.layoutParams as ViewGroup.LayoutParams).apply {
            width = if(progress <= 20) requireContext().toPx(20).toInt() else requireContext().toPx(progress).toInt()
        }
        binding.tvHomeProgress.text = "${((1 - (currentSize.toFloat() / size.toFloat())) * 100).toInt()}%"
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

}

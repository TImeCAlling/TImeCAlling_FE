package com.umc.timeCAlling.presentation.addSchedule

import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.DayViewDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleSecondBinding
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.presentation.addSchedule.adapter.CategoryRVA
import com.umc.timeCAlling.presentation.addSchedule.adapter.SearchResultRVA
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleSecondFragment: BaseFragment<FragmentAddScheduleSecondBinding>(R.layout.fragment_add_schedule_second) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private lateinit var repeatBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var categoryBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun initView() {

        initCategoryList()
        bottomNavigationRemove()
        initRepeatBottomSheet()
        initCategoryBottomSheet()
        moveToAddScheduleSuccess()

        binding.ivAddScheduleSecondBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun initRepeatBottomSheet() {
        val calendarView = binding.calendarView // MaterialCalendarView
        val startTextView = binding.tvAddScheduleRepeatStart // 날짜를 표시할 TextView
        val endTextView = binding.tvAddScheduleRepeatEnd // 날짜를 표시할 TextView
        var startDate: CalendarDay? = null
        var endDate: CalendarDay? = null

        val bottomSheet = binding.bottomSheetRepeat // BottomSheet 레이아웃 ID
        repeatBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        repeatBottomSheetBehavior.peekHeight = 0
        repeatBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        repeatBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                if (startDate == null) {
                    startDate = date
                } else if (endDate == null) {
                    endDate = date
                    // 시작일과 종료일이 선택되었으므로 범위를 표시합니다.
                    calendarView.selectRange(startDate, endDate)
                } else {
                    // 시작일과 종료일이 이미 선택되어 있으므로 초기화하고 새 범위를 시작합니다.
                    startDate = date
                    endDate = null
                    calendarView.clearSelection()
                    calendarView.setDateSelected(date, true)
                }
            }
        }

            // 이미지 클릭 리스너
        binding.ivAddScheduleRepeat.setOnClickListener {
            if (repeatBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 시간 BottomSheet 숨기기
                binding.viewBottomSheetBackground.visibility = View.VISIBLE
            } else {
                repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.INVISIBLE
            }
        }

        repeatBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= 0) {
                    binding.viewBottomSheetBackground.visibility = View.INVISIBLE
                } else if (slideOffset > 0) {
                    binding.viewBottomSheetBackground.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // onStateChanged()에서는 visibility를 변경하지 않음
            }
        })
    }

    private fun initCategoryBottomSheet() {
        val bottomSheet = binding.bottomSheetCategory // BottomSheet 레이아웃 ID
        categoryBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        categoryBottomSheetBehavior.peekHeight = 0
        categoryBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        categoryBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        // 이미지 클릭 리스너
        binding.ivAddScheduleCategoryList.setOnClickListener {
            if (categoryBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.VISIBLE
            } else {
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.INVISIBLE
            }
        }

        binding.ivBottomSheetCategoryEdit.setOnClickListener {
            moveToCategoryEdit()
        }

        categoryBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= 0) {
                    binding.viewBottomSheetBackground.visibility = View.INVISIBLE
                } else if (slideOffset > 0) {
                    binding.viewBottomSheetBackground.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // onStateChanged()에서는 visibility를 변경하지 않음
            }
        })
    }

    private fun initCategoryList() {
        initCategoryBottomSheet()
        binding.bottomSheetCategory.visibility = View.VISIBLE
        binding.bottomSheetRepeat.visibility = View.GONE

        val bottomSheetCategoryRVA = CategoryRVA(
            viewModel,
            viewLifecycleOwner,
            viewLifecycleOwner,
            findNavController()
        )
        binding.rvBottomSheetCategory.adapter = bottomSheetCategoryRVA
        binding.rvBottomSheetCategory.layoutManager = LinearLayoutManager(requireContext())
        Log.d("LocationSearchFragment", "결과")
    }

    private fun moveToCategoryEdit() {
        binding.ivBottomSheetCategoryEdit.setOnClickListener {
            findNavController().navigate(R.id.action_addScheduleSecondFragment_to_categoryEditFragment)
        }
    }

    private fun moveToAddScheduleSuccess() {
        binding.tvAddScheduleNext.setOnClickListener {
            findNavController().navigate(R.id.action_addScheduleSecondFragment_to_addScheduleSuccessFragment)
        }
    }

}
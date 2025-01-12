package com.umc.timeCAlling.presentation.addSchedule

import android.content.Context
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.databinding.adapters.ViewBindingAdapter.setPadding
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
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class AddScheduleSecondFragment: BaseFragment<FragmentAddScheduleSecondBinding>(R.layout.fragment_add_schedule_second) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private lateinit var repeatBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var categoryBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun initView() {

        binding.viewBottomSheetBackground.isClickable = false

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

        val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")
        val selectedDays = mutableSetOf<String>() // 선택된 요일을 저장할 Set
        val dayTextViews = daysOfWeek.map { dayOfWeek ->
            TextView(requireContext()).apply {
                text = dayOfWeek
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    val marginInPx = if (dayOfWeek != "월") {
                        8.dpToPx(requireContext())
                    } else {
                        0
                    }
                    setMargins(marginInPx, 0, 0, 0)
                }
                gravity = Gravity.CENTER
                setPadding(14.dpToPx(requireContext()), 14.dpToPx(requireContext()), 14.dpToPx(requireContext()), 14.dpToPx(requireContext()))
                setBackgroundResource(R.drawable.shape_rect_8_gray300_fill) // 초기 배경 설정
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

                setOnClickListener {
                    // 배경 변경 및 선택 상태 업데이트
                    if (selectedDays.contains(dayOfWeek)) { // 이미 선택된 경우
                        selectedDays.remove(dayOfWeek) // 선택 취소
                        setBackgroundResource(R.drawable.shape_rect_8_gray300_fill) // 배경 변경
                    } else { // 선택되지 않은 경우
                        selectedDays.add(dayOfWeek) // 선택 추가
                        setBackgroundResource(R.drawable.shape_rect_8_mint300_fill_mint_line_1) // 배경 변경
                    }
                }
            }
        }

        binding.layoutBottomSheetRepeatDof.removeAllViews()
        dayTextViews.forEach { textView ->
            binding.layoutBottomSheetRepeatDof.addView(textView)
        }

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
                    calendarView.selectRange(startDate, endDate)
                } else {
                    startDate = date
                    endDate = null
                    calendarView.clearSelection()
                    calendarView.setDateSelected(date, true)
                }
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            binding.tvBottomSheetRepeatStart.text = startDate?.date?.format(formatter) ?: ""
            binding.tvBottomSheetRepeatEnd.text = endDate?.date?.format(formatter) ?: ""
        }

        binding.ivAddScheduleRepeat.setOnClickListener {
            if (repeatBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

        val bottomSheetCategoryRVA = CategoryRVA(
            requireContext(),
            viewModel,
            viewLifecycleOwner,
            viewLifecycleOwner,
            findNavController()
        )
        binding.rvBottomSheetCategory.adapter = bottomSheetCategoryRVA
        binding.rvBottomSheetCategory.layoutManager = LinearLayoutManager(requireContext())
        Log.d("LocationSearchFragment", "결과")
    }

    private fun Int.dpToPx(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
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
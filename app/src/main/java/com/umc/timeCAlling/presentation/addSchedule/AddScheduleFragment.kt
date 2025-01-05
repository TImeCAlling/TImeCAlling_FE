package com.umc.timeCAlling.presentation.addSchedule

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.navigation.fragment.dialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment: BaseFragment<FragmentAddScheduleBinding>(R.layout.fragment_add_schedule) {

    private lateinit var dateBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var timeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun initView() {

        binding.viewBottomSheetBackground.isClickable = false

        bottomNavigationRemove()
        initDateBottomSheet()
        initTimeBottomSheet()
        initScheduleName()
        initScheduleMemo()

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
        var selectedDate: String? = null // 선택된 날짜를 저장할 변수

        val bottomSheet = binding.bottomSheetDate // BottomSheet 레이아웃 ID
        dateBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        dateBottomSheetBehavior.peekHeight = 0
        dateBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        dateBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDate = "${date.year}년 ${date.month + 1}월 ${date.day}일" // 선택한 날짜 형식 지정
            }
        }

        // 저장 버튼 클릭 리스너
        binding.tvAddScheduleDateSave.setOnClickListener { // 저장 버튼 ID를 확인하고 수정해야 할 수 있습니다.
            selectedDate?.let {
                dateTextView.text = it // TextView에 날짜 설정
                binding.layoutAddScheduleDate.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_mint_line_2) // 배경 변경
                binding.tvAddScheduleDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900)) // 글자 색깔 변경
                binding.ivAddScheduleDate.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_calender_check)) // 아이콘 변경

                dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // BottomSheet 숨기기
            }
        }

        // 이미지 클릭 리스너
        binding.layoutAddScheduleDate.setOnClickListener {
            if (dateBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 시간 BottomSheet 숨기기
                binding.viewBottomSheetBackground.visibility = View.VISIBLE
            } else {
                dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.INVISIBLE
            }
        }

        dateBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

    private fun initTimeBottomSheet() {

        val numberPickerHour = binding.numberPickerHour // 시간 NumberPicker
        val numberPickerMinute = binding.numberPickerMinute // 분 NumberPicker
        val timeTextView = binding.tvAddScheduleTime // 시간을 표시할 TextView

        val bottomSheet = binding.bottomSheetTime // BottomSheet 레이아웃 ID
        timeBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        timeBottomSheetBehavior.peekHeight = 0
        timeBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        timeBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        binding.tvAddScheduleTimeSave.setOnClickListener { // 저장하기 버튼 클릭 리스너
            val selectedHour = numberPickerHour.value // 선택한 시간
            val selectedMinute = numberPickerMinute.value // 선택한 분

            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute) // 시간 형식 지정
            timeTextView.text = selectedTime // TextView에 시간 설정
            binding.layoutAddScheduleTime.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_mint_line_2) // 배경 변경
            binding.tvAddScheduleTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900)) // 글자 색깔 변경
            binding.ivAddScheduleTime.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_calender_check)) // 아이콘 변경

            timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // BottomSheet 숨기기
        }

        // 이미지 클릭 리스너
        binding.layoutAddScheduleTime.setOnClickListener {
            if (timeBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.VISIBLE
            } else {
                timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.INVISIBLE
            }
        }

        timeBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

    private fun initScheduleName(){
        binding.etAddScheduleName.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10)) // 최대 글자 수 10글자로 제한

        binding.etAddScheduleName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0 // 입력된 글자 수
                binding.tvAddScheduleTitleCount.text = textLength.toString() // 글자 수 표시

                if (textLength > 0) { // 글자가 입력된 경우
                    binding.layoutAddScheduleName.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_mint_line_2) // 배경 변경
                    binding.etAddScheduleName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900)) // 글자 색깔 변경
                    binding.ivAddScheduleNameCheck.visibility = View.VISIBLE // 체크 표시 보이기
                } else { // 글자가 없는 경우
                    binding.layoutAddScheduleName.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_gray250_fill) // 배경 변경
                    binding.etAddScheduleName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_600)) // 글자 색깔 변경
                    binding.ivAddScheduleNameCheck.visibility = View.INVISIBLE // 체크 표시 숨기기
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후
            }
        })

    }

    private fun initScheduleMemo(){
        binding.etAddScheduleMemo.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 최대 글자 수 10글자로 제한

        binding.etAddScheduleMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0 // 입력된 글자 수
                binding.tvAddScheduleMemoCount.text = textLength.toString() // 글자 수 표시

                if (textLength > 0) { // 글자가 입력된 경우
                    binding.layoutAddScheduleMemo.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_mint_line_2) // 배경 변경
                    binding.etAddScheduleMemo.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900)) // 글자 색깔 변경
                    binding.ivAddScheduleMemoCheck.visibility = View.VISIBLE // 체크 표시 보이기
                } else { // 글자가 없는 경우
                    binding.layoutAddScheduleMemo.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_gray250_fill) // 배경 변경
                    binding.etAddScheduleMemo.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_600)) // 글자 색깔 변경
                    binding.ivAddScheduleMemoCheck.visibility = View.INVISIBLE // 체크 표시 숨기기
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후
            }
        })

    }
}
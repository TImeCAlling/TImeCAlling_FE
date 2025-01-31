package com.umc.timeCAlling.presentation.addSchedule

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.semantics.text
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentAddScheduleBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment: BaseFragment<FragmentAddScheduleBinding>(R.layout.fragment_add_schedule) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private lateinit var dateBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var timeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var scheduleId : Int = -1

    override fun initObserver() {

    }

    override fun initView() {
        scheduleId = arguments?.getInt("scheduleId") ?: -1

        if (scheduleId != -1) { binding.tvAddScheduleTitle.text = "일정수정" } else { binding.tvAddScheduleTitle.text = "일정추가" }

        initSavedData()

        binding.viewBottomSheetBackground.isClickable = false

        bottomNavigationRemove()
        initDateBottomSheet()
        initTimeBottomSheet()
        initScheduleName()
        initScheduleMemo()
        moveToLocationSearch()
        moveToAddScheduleSecond()

        binding.ivAddScheduleBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }
        viewModel.searchLocation.observe(viewLifecycleOwner) { locations ->
            binding.tvAddScheduleLocation.text = locations[0].name
            binding.ivAddScheduleLocation.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_900))
            binding.tvAddScheduleLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
        }
        viewModel.moveTime.observe(viewLifecycleOwner) { moveTime ->
            binding.tvAddScheduleHour.text = if (moveTime >= 60) (moveTime / 60).toString() else "0"
            binding.tvAddScheduleMinute.text = (moveTime%60).toString()

            if (moveTime != null) {
                binding.tvAddScheduleTimeTaken.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_gray900_fill)
                binding.tvAddScheduleTimeTaken.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        // + 버튼 숨기기
        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }


    private fun initSavedData() {
        // ViewModel에서 데이터를 가져와 UI에 설정
        binding.etAddScheduleName.setText(viewModel.scheduleName.value)
        binding.etAddScheduleMemo.setText(viewModel.scheduleMemo.value)
        viewModel.scheduleDate.observe(viewLifecycleOwner) { scheduleDate ->
            if (scheduleDate.isNullOrEmpty()) {
                binding.tvAddScheduleDate.text = "날짜를 입력하세요"
            } else {
                binding.tvAddScheduleDate.text = scheduleDate
            }
        }
        viewModel.scheduleTime.observe(viewLifecycleOwner) { scheduleTime ->
            if (scheduleTime.isNullOrEmpty()) {
                binding.tvAddScheduleTime.text = "시간을 입력하세요"
            } else {
                binding.tvAddScheduleTime.text = scheduleTime
            }
        }
        // 위치 정보 설정 (viewModel.searchLocation)
        viewModel.searchLocation.value?.let { locations ->
            if (locations.isNotEmpty()) {
                binding.tvAddScheduleLocation.text = locations[0].name
                binding.ivAddScheduleLocation.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_900))
                binding.tvAddScheduleLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
            }
        }

        // 이동 시간 설정 (viewModel.moveTime)
        viewModel.moveTime.value?.let { moveTime ->
            binding.tvAddScheduleHour.text = if (moveTime >= 60) (moveTime / 60).toString() else "0"
            binding.tvAddScheduleMinute.text = (moveTime % 60).toString()
            if (moveTime != 0) { // 0이 아닌 경우에만 배경 변경
                binding.tvAddScheduleTimeTaken.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_gray900_fill)
                binding.tvAddScheduleTimeTaken.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }

        // observe 제거
        viewModel.scheduleName.removeObservers(viewLifecycleOwner)
        viewModel.scheduleMemo.removeObservers(viewLifecycleOwner)
        viewModel.scheduleDate.removeObservers(viewLifecycleOwner)
        viewModel.scheduleTime.removeObservers(viewLifecycleOwner)
        viewModel.searchLocation.removeObservers(viewLifecycleOwner)
        viewModel.moveTime.removeObservers(viewLifecycleOwner)
    }

    private fun initDateBottomSheet() {
        val calendarView = binding.calendarView // MaterialCalendarView
        val dateTextView = binding.tvAddScheduleDate // 날짜를 표시할 TextView
        var selectedDate: String? = null // 선택된 날짜를 저장할 변수
        var formattedDate: String? = null
        val bottomSheet = binding.bottomSheetDate // BottomSheet 레이아웃 ID
        dateBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        dateBottomSheetBehavior.peekHeight = 0
        dateBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        dateBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDate = "${date.year}년 ${date.month}월 ${date.day}일" // 선택한 날짜 형식 지정
                formattedDate = String.format("%d-%02d-%02d", date.year, date.month, date.day)
            }
        }

        // 저장 버튼 클릭 리스너
        binding.tvAddScheduleDateSave.setOnClickListener { // 저장 버튼 ID를 확인하고 수정해야 할 수 있습니다.
            selectedDate?.let {
                dateTextView.text = it // TextView에 날짜 설정
                binding.ivAddScheduleDate.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_black)) // 아이콘 변경
                binding.tvAddScheduleDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
                binding.ivAddScheduleDateArrow.visibility = View.VISIBLE

                dateBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // BottomSheet 숨기기

                viewModel.setScheduleDate(formattedDate!!)
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

        val numberPickerAmPm = binding.numberPickerAmPm // 오전/오후 NumberPicker
        val numberPickerHour = binding.numberPickerHour // 시간 NumberPicker
        val numberPickerMinute = binding.numberPickerMinute // 분 NumberPicker
        val timeTextView = binding.tvAddScheduleTime // 시간을 표시할 TextView

        val bottomSheet = binding.bottomSheetTime // BottomSheet 레이아웃 ID
        timeBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 초기 상태 숨김
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        val amPmValues = arrayOf("오전", "오후")
        numberPickerAmPm.displayedValues = amPmValues

        timeBottomSheetBehavior.peekHeight = 0
        timeBottomSheetBehavior.isHideable = true // 드래그하여 숨기기 설정
        timeBottomSheetBehavior.skipCollapsed = true // 숨김 상태로 바로 전환

        binding.tvAddScheduleTimeSave.setOnClickListener { // 저장하기 버튼 클릭 리스너
            val selectedHour = numberPickerHour.value // 선택한 시간
            val selectedMinute = numberPickerMinute.value // 선택한 분
            val selectedAmPm = amPmValues[numberPickerAmPm.value] // 오전/오후 문자열 가져오기
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            val selectedTime = String.format("%s %02d:%02d", selectedAmPm, selectedHour, selectedMinute) // 시간 형식 지정
            timeTextView.text = selectedTime // TextView에 시간 설정
            binding.ivAddScheduleTime.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_alarm_black)) // 아이콘 변경
            binding.tvAddScheduleTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
            binding.ivAddScheduleTimeArrow.visibility = View.VISIBLE

            timeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // BottomSheet 숨기기

            viewModel.setScheduleTime(formattedTime)
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

                viewModel.setScheduleName(s.toString())
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
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                binding.tvAddScheduleMemoCount.text = textLength.toString()

                viewModel.setScheduleMemo(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun moveToLocationSearch() {
        binding.layoutAddScheduleLocation.setOnClickListener {
            findNavController().navigate(R.id.action_addScheduleFragment_to_locationSearchFragment)
        }
    }

    private fun moveToAddScheduleSecond() {
        binding.tvAddScheduleNext.setOnClickListener {
            val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
            findNavController().navigate(R.id.action_addScheduleFragment_to_addScheduleSecondFragment, bundle)
        }
    }
}
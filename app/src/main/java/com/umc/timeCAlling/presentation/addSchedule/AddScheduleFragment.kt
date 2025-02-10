package com.umc.timeCAlling.presentation.addSchedule

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.intl.Locale
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
import java.text.SimpleDateFormat
import kotlin.text.format
import kotlin.text.substring

@AndroidEntryPoint
class AddScheduleFragment: BaseFragment<FragmentAddScheduleBinding>(R.layout.fragment_add_schedule) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private lateinit var dateBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var timeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var scheduleId : Int = -1
    private var mode : String = ""
    private var location : Boolean = false

    override fun initObserver() {

    }

    override fun initView() {
        mode = viewModel.getMode()
        location = viewModel.getLocation()
        scheduleId = arguments?.getInt("scheduleId") ?: -1
        if(location==false){
            viewModel.setScheduleId(scheduleId)
        }
        Log.d("AddScheduleFragment", "scheduleId: $scheduleId")

        if (scheduleId != -1) { binding.tvAddScheduleTitle.text = "일정수정" } else { binding.tvAddScheduleTitle.text = "일정추가" }

        initSavedData()

        if (mode == "shared") {
            binding.tvAddScheduleTitle.text = "일정추가"
            binding.etAddScheduleName.setText(viewModel.scheduleName.value)
            binding.etAddScheduleName.isEnabled = false
            binding.viewAddScheduleName.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_400))
            binding.tvAddScheduleTitleCount.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_400))
            // 날짜 표시 형식 변경
            viewModel.scheduleDate.value?.let { date ->
                val year = date.substring(0, 4)
                val month = date.substring(5, 7)
                val day = date.substring(8, 10)
                binding.tvAddScheduleDate.text = String.format("%s년 %s월 %s일", year, month, day)
            }
            binding.layoutAddScheduleDate.setBackgroundResource(R.drawable.shape_rect_10_gray300_fill_gray400_line_1)
            // 시간 표시 형식 변경
            viewModel.scheduleTime.value?.let { time ->
                val hour = time.substring(0, 2).toInt()
                val minute = time.substring(3, 5)
                val amPm = if (hour >= 12) "오후" else "오전"
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val selectedTime = String.format("%s %02d:%s", amPm, displayHour, minute)
                binding.tvAddScheduleTime.text = selectedTime
            }

            binding.layoutAddScheduleTime.setBackgroundResource(R.drawable.shape_rect_10_gray300_fill_gray400_line_1)
            binding.tvAddScheduleLocation.text = viewModel.selectedLocationName.value
            binding.bottomSheetTime.visibility = View.GONE
            binding.bottomSheetDate.visibility = View.GONE
        }else{
            initDateBottomSheet()
            initTimeBottomSheet()
        }
        binding.viewBottomSheetBackground.isClickable = false

        bottomNavigationRemove()
        initScheduleName()
        initScheduleMemo()
        moveToLocationSearch()
        moveToAddScheduleSecond()

        binding.ivAddScheduleBack.setOnSingleClickListener {
            findNavController().popBackStack()
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
        viewModel.scheduleDate.value?.let { date ->
            if (date.isNotEmpty()) {
                binding.tvAddScheduleDate.text = date
                binding.ivAddScheduleDate.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_black))
                binding.tvAddScheduleDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
                binding.ivAddScheduleDateArrow.visibility = View.VISIBLE
            }
        }
        viewModel.scheduleTime.value?.let { time ->
            if (time.isNotEmpty()) {
                val hour = time.substring(0, 2).toInt()
                val minute = time.substring(3, 5)
                val amPm = if (hour >= 12) "오후" else "오전"
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val selectedTime = String.format("%s %02d:%s", amPm, displayHour, minute)
                binding.tvAddScheduleTime.text = selectedTime
                binding.tvAddScheduleTime.text = time
                binding.ivAddScheduleTime.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_alarm_black))
                binding.tvAddScheduleTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
                binding.ivAddScheduleTimeArrow.visibility = View.VISIBLE
            }
        }
        viewModel.searchLocation.value?.let { locations ->
            if (locations.isNotEmpty()) {
                binding.tvAddScheduleLocation.text = locations[0].name
                binding.ivAddScheduleLocation.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_900))
                binding.tvAddScheduleLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_900))
            }
        }
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
        if(mode == "shared"){
            if(binding.etAddScheduleMemo.text.isNotEmpty()&&binding.tvAddScheduleMinute.text.isNotEmpty()){
                binding.tvAddScheduleNext.isEnabled = true
                binding.tvAddScheduleNext.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.mint_main))
                binding.tvAddScheduleNext.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.tvAddScheduleNext.setOnClickListener {
                    val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
                    findNavController().navigate(
                        R.id.action_addScheduleFragment_to_addScheduleSecondFragment,
                        bundle
                    )
                }
            }
        }
        if(binding.etAddScheduleName.text.isNotEmpty()&& binding.etAddScheduleMemo.text.isNotEmpty()&& binding.tvAddScheduleDate.text.isNotEmpty()&& binding.tvAddScheduleTime.text.isNotEmpty()&& binding.tvAddScheduleLocation.text.isNotEmpty()&& binding.tvAddScheduleMinute.text.isNotEmpty()) {
            binding.tvAddScheduleNext.isEnabled = true
            binding.tvAddScheduleNext.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.mint_main))
            binding.tvAddScheduleNext.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.tvAddScheduleNext.setOnClickListener {
                scheduleId = viewModel.scheduleId.value ?: -1
                val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
                findNavController().navigate(
                    R.id.action_addScheduleFragment_to_addScheduleSecondFragment,
                    bundle
                )
            }
        }else{ binding.tvAddScheduleNext.isEnabled = false }
    }
}
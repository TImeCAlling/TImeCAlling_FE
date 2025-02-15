package com.umc.timeCAlling.presentation.addSchedule

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.ui.semantics.text
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.materialswitch.MaterialSwitch
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category
import com.umc.timeCAlling.databinding.FragmentAddScheduleSecondBinding
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager.getCategoryByName
import com.umc.timeCAlling.presentation.addSchedule.adapter.CategoryRVA
import com.umc.timeCAlling.presentation.alarm.AlarmHelper
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class AddScheduleSecondFragment: BaseFragment<FragmentAddScheduleSecondBinding>(R.layout.fragment_add_schedule_second) {

    private val viewModel: AddScheduleViewModel by activityViewModels()
    private lateinit var repeatBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var categoryBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isRepeatEnabled = false
    private var selectedDays = mutableListOf<String>()
    private var scheduleId : Int = -1
    private var mode : String = ""
    private lateinit var spf: SharedPreferences

    override fun initView() {
        binding.apply {
            menuAddScheduleRepeat.setOnCheckedChangeListener { _, isChecked -> setSwitchColor(binding.menuAddScheduleRepeat, isChecked)}
        }
        spf = requireContext().getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        mode = viewModel.getMode()
        scheduleId = arguments?.getInt("scheduleId") ?: -2
        Log.d("AddScheduleSecondFragment", "scheduleId: $scheduleId")

        initSavedData()

        if(mode == "shared"){
            binding.tvAddScheduleSecondTitle.text = "일정추가"
            binding.layoutAddScheduleRepeat.setBackgroundResource(R.drawable.shape_rect_10_gray300_fill_gray400_line_1)
            binding.tvAddScheduleRepeat.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
            binding.ivAddScheduleRepeat.isEnabled = false
            viewModel.repeatDates.value?.let { repeatDates ->
                if (repeatDates.isNotEmpty()) {
                    val koreanDays = repeatDates.map { day ->
                        when (day) {
                            "MONDAY" -> "월요일"
                            "TUESDAY" -> "화요일"
                            "WEDNESDAY" -> "수요일"
                            "THURSDAY" -> "목요일"
                            "FRIDAY" -> "금요일"
                            "SATURDAY" -> "토요일"
                            "SUNDAY" -> "일요일"
                            else -> day
                        }
                    }
                    binding.tvAddScheduleRepeat.text = "${koreanDays.joinToString(", ")} 마다"
                    binding.menuAddScheduleRepeat.isChecked = true
                }
            }
            viewModel.startDate.value?.let { startDate ->
                binding.tvAddScheduleRepeatStart.text = startDate
            }
            viewModel.endDate.value?.let { endDate ->
                binding.tvAddScheduleRepeatEnd.text = endDate
            }
            viewModel.isRepeat.value?.let { isRepeat ->
                isRepeatEnabled = isRepeat
                binding.menuAddScheduleRepeat.isChecked = isRepeat
                viewModel.setIsRepeat(isRepeat)
                Log.d("AddScheduleSecondFragment", "isRepeat: $isRepeat")
                binding.layoutAddSheduleRepeatDate.visibility = if (isRepeat) View.VISIBLE else View.GONE
            }
            binding.bottomSheetRepeat.visibility = View.GONE
            repeatBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetRepeat)
        }else{
            initRepeatBottomSheet()
            initRepeatSwitch()
        }
        scheduleId = arguments?.getInt("scheduleId") ?: -1

        if (scheduleId != -1) { binding.tvAddScheduleSecondTitle.text = "일정수정" } else { binding.tvAddScheduleSecondTitle.text = "일정추가" }

        binding.tvAddScheduleNext.isEnabled = true
        moveToAddScheduleSuccess()
        binding.viewBottomSheetBackground.isClickable = false
        initCategoryList()
        bottomNavigationRemove()
        initCategoryBottomSheet()
        initSpareTimeTextViews()

        binding.ivAddScheduleSecondBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initObserver() {/*
        viewModel.selectedCategory.observe(viewLifecycleOwner, Observer { categoryName ->
            updateCategoryUI(getCategoryByName(categoryName))
        })*/
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

    private fun initSavedData(){

        viewModel.freeTime.value?.let { freeTime ->
            if(freeTime.isNotEmpty()) {
                val spareTimeTextViews = listOf(
                    binding.tvAddScheduleSecondSpareTime1,
                    binding.tvAddScheduleSecondSpareTime2,
                    binding.tvAddScheduleSecondSpareTime3
                )
                spareTimeTextViews.forEach { tv ->
                    val freeTimeText = when (freeTime) {
                        "TIGHT" -> "딱딱"
                        "RELAXED" -> "여유"
                        "PLENTY" -> "넉넉"
                        else -> ""
                    }
                    if (tv.text.toString() == freeTimeText) {
                        tv.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.shape_rect_999_mint300_fill_mint_line_1
                        )
                        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.mint_main))
                    } else {
                        tv.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.shape_rect_999_gray200_fill
                        )
                        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
                    }
                }
            }
        }
        viewModel.isRepeat.value?.let { isRepeat ->
            if (isRepeat != null) {
                binding.menuAddScheduleRepeat.isChecked = isRepeat
            }
        }
        viewModel.repeatDates.value?.let { repeatDates ->
            if (repeatDates.isNotEmpty()) {
                selectedDays.clear()
                selectedDays.addAll(repeatDates.map { day ->
                    when (day) {
                        "MONDAY" -> "월"
                        "TUESDAY" -> "화"
                        "WEDNESDAY" -> "수"
                        "THURSDAY" -> "목"
                        "FRIDAY" -> "금"
                        "SATURDAY" -> "토"
                        "SUNDAY" -> "일"
                        else -> day
                    }
                })
                updateRepeatInfo()
            }
        }
        viewModel.startDate.value?.let { startDate ->
            if (startDate.isNotEmpty()) {
                binding.tvAddScheduleRepeatStart.text = startDate
            }
        }
        viewModel.endDate.value?.let { endDate ->
            if (endDate.isNotEmpty()) {
                binding.tvAddScheduleRepeatEnd.text = endDate
            }
        }
        viewModel.categoryName.value?.let { categoryName ->
            if (categoryName.isNotEmpty()) {
                updateCategoryUI(getCategoryByName(categoryName))
            }
        }
    }
    private fun initSpareTimeTextViews() {
        val spareTimeTextViews = listOf(
            binding.tvAddScheduleSecondSpareTime1,
            binding.tvAddScheduleSecondSpareTime2,
            binding.tvAddScheduleSecondSpareTime3
        )

        spareTimeTextViews.forEach { textView ->
            textView.setOnClickListener { clickedTextView ->
                spareTimeTextViews.forEach { tv ->
                    if (tv == clickedTextView) {
                        tv.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_mint300_fill_mint_line_1)
                        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.mint_main))
                        val freeTime = when (tv.text.toString()) { // 텍스트 변환
                            "딱딱" -> "TIGHT"
                            "여유" -> "RELAXED"
                            "넉넉" -> "PLENTY"
                            else -> ""
                        }
                        viewModel.setFreeTime(freeTime) // 변환된 값 전달
                        } else {
                        tv.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_gray200_fill)
                        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
                    }
                }
            }
        }
    }

    private fun initRepeatSwitch() {
        binding.menuAddScheduleRepeat.setOnCheckedChangeListener { _, isChecked ->
            isRepeatEnabled = isChecked
            viewModel.setIsRepeat(isRepeatEnabled)
            updateRepeatInfo()
        }
    }

    private fun updateRepeatInfo() {
        val repeatText = if (isRepeatEnabled) {
            if (selectedDays.isNotEmpty()) {
                val selectedDaysOfWeek = selectedDays.joinToString(", ") { day ->
                    when (day) {
                        "월" -> "월요일"
                        "화" -> "화요일"
                        "수" -> "수요일"
                        "목" -> "목요일"
                        "금" -> "금요일"
                        "토" -> "토요일"
                        "일" -> "일요일"
                        else -> day
                    }
                }
                "$selectedDaysOfWeek 마다"
            } else {
                "없음"
            }
        } else {
            "없음"
        }
        binding.tvAddScheduleRepeat.text = repeatText
        val selectedDaysEnglish = selectedDays.map { day ->
            when (day) {
                "월" -> "MONDAY"
                "화" -> "TUESDAY"
                "수" -> "WEDNESDAY"
                "목" -> "THURSDAY"
                "금" -> "FRIDAY"
                "토" -> "SATURDAY"
                "일" -> "SUNDAY"
                else -> day
            }
        }
        viewModel.setRepeatDates(selectedDaysEnglish)
    }

    private fun initRepeatBottomSheet() {
        val calendarView = binding.calendarView // MaterialCalendarView
        val startTextView = binding.tvAddScheduleRepeatStart // 날짜를 표시할 TextView
        val endTextView = binding.tvAddScheduleRepeatEnd // 날짜를 표시할 TextView
        var startDate: CalendarDay? = null
        var endDate: CalendarDay? = null

        val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")
        val selectedDaysSet = mutableSetOf<String>() // 선택된 요일을 저장할 Set
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
                    if (selectedDaysSet.contains(dayOfWeek)) { // 이미 선택된 경우
                        selectedDaysSet.remove(dayOfWeek) // 선택 취소
                        setBackgroundResource(R.drawable.shape_rect_8_gray300_fill) // 배경 변경
                    } else { // 선택되지 않은 경우
                        selectedDaysSet.add(dayOfWeek) // 선택 추가
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
            }
        })

        binding.tvAddScheduleRepeatSave.setOnClickListener {
            this@AddScheduleSecondFragment.selectedDays.clear()
            this@AddScheduleSecondFragment.selectedDays.addAll(selectedDaysSet)
            updateRepeatInfo()
            binding.menuAddScheduleRepeat.isChecked = true
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            startTextView.text = startDate?.date?.format(formatter) ?: ""
            endTextView.text = endDate?.date?.format(formatter) ?: ""

            binding.layoutAddSheduleRepeatDate.visibility = View.VISIBLE
            repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            viewModel.setStartDate(startTextView.text.toString())
            viewModel.setEndDate(endTextView.text.toString())
        }
    }

    private fun initCategoryBottomSheet() {
        val bottomSheet = binding.bottomSheetCategory
        categoryBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.viewBottomSheetBackground.visibility = View.INVISIBLE

        categoryBottomSheetBehavior.peekHeight = 0
        categoryBottomSheetBehavior.isHideable = true
        categoryBottomSheetBehavior.skipCollapsed = true

        binding.ivAddScheduleCategoryList.setOnSingleClickListener {
            if (categoryBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                repeatBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.VISIBLE
            } else {
                categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.viewBottomSheetBackground.visibility = View.INVISIBLE
            }
        }

        binding.ivBottomSheetCategoryEdit.setOnSingleClickListener {
            moveToCategoryEdit()
        }


        val categoryBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                        Lifecycle.State.INITIALIZED
                    )
                ) {
                    if (slideOffset <= 0) {
                        binding.viewBottomSheetBackground.visibility =
                            View.INVISIBLE
                    } else if (slideOffset > 0) {
                        binding.viewBottomSheetBackground.visibility = View.VISIBLE
                    }
                }
            }


            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        }

        binding.tvAddScheduleCategorySave.setOnClickListener {
            categoryBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            categoryBottomSheetBehavior.removeBottomSheetCallback(categoryBottomSheetCallback)
            categoryBottomSheetBehavior.addBottomSheetCallback(categoryBottomSheetCallback)
            viewModel.categoryName.value?.let {
                updateCategoryUI(getCategoryByName(it))
            }
        }
    }

    private fun initCategoryList() {
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

    private fun updateCategoryUI(category: Category?) {
        if (category == null) {
            binding.ivAddScheduleCategoryLogo.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_600)
            binding.ivAddScheduleCategoryList.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gray_600)
            binding.tvAddScheduleCategory.text = "없음"
            binding.tvAddScheduleCategory.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
        } else {
            moveToAddScheduleSuccess()
            val categoryColor = CategoryManager.getColor(category.color) // 수정된 부분
            binding.ivAddScheduleCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), categoryColor)) // 수정된 부분
            binding.ivAddScheduleCategoryList.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), categoryColor)) // 수정된 부분
            binding.tvAddScheduleCategory.text = category.name
            binding.tvAddScheduleCategory.setTextColor(ContextCompat.getColor(requireContext(), categoryColor)) // 수정된 부분
            viewModel.setCategoryName(category.name)
            viewModel.setCategoryColor(category.color) // 수정된 부분
        }
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
            scheduleId = arguments?.getInt("scheduleId") ?: -1
            val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
            findNavController().navigate(R.id.action_addScheduleSecondFragment_to_addScheduleSuccessFragment, bundle)
            if (mode == "shared") {
                viewModel.createSharedSchedule(scheduleId)
            } else {
                if (scheduleId == -1) {
                    viewModel.createSchedule()
                    // 권한 요청
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (!requireContext().getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
                            requestExactAlarmPermission()
                            return@setOnClickListener
                        }
                    }
                    setAlarm()
                } else {
                    viewModel.editSchedule(scheduleId)
                }
            }
            viewModel.setMode("")
        }
    }

    private fun setAlarm() {
        val alarmHelper = AlarmHelper(requireContext())
        val alarmName = viewModel.scheduleName.value ?: "Unknown Alarm"

        // scheduleDate와 scheduleTime을 사용하여 알람 시간 설정
        val scheduleDate = viewModel.scheduleDate.value
        val scheduleTime = viewModel.scheduleTime.value

        var alarmId = spf.getInt("lastAlarmId", 0)

        alarmId++

        with(spf.edit()) {
            putInt("lastAlarmId", alarmId)
            apply()
        }

        if (scheduleDate != null && scheduleTime != null) {
            val (year, month, dayOfMonth) = parseDate(scheduleDate)
            val (hourOfDay, minute) = parseTime(scheduleTime)

            alarmHelper.setAlarm(alarmName, year, month, dayOfMonth, hourOfDay, minute, alarmId)

            /* // 반복 설정 여부 확인
             if (viewModel.isRepeat.value == true) {
                 // 반복 알람 설정
                 val startDate = viewModel.startDate.value ?: ""
                 val endDate = viewModel.endDate.value ?: ""
                 val repeatDays = viewModel.repeatDates.value ?: emptyList()
                 Log.d("AddScheduleSecondFragment", "Setting repeating alarm for Schedule ID: $alarmId")
                 alarmHelper.setRepeatingAlarm(alarmName, startDate, endDate, repeatDays, hourOfDay, minute, alarmId)
                 Log.d("AddScheduleSecondFragment", "Repeating alarm set for Schedule ID: $alarmId")
             } else {
                 // 일반 알람 설정
                 Log.d("AddScheduleSecondFragment", "Setting alarm for Schedule ID: $alarmId")
                 alarmHelper.setAlarm(alarmName, year, month, dayOfMonth, hourOfDay, minute, alarmId)
                 Log.d("AddScheduleSecondFragment", "Alarm set for Schedule ID: $alarmId")
             }*/
        } else {
            Log.e("AddScheduleSecondFragment", "scheduleDate or scheduleTime is null")
        }
    }

    private fun setSwitchColor(switch: MaterialSwitch, isChecked: Boolean) {
        if(isChecked) {
            switch.trackTintList = getResources().getColorStateList(R.color.mint_main)
        } else {
            switch.trackTintList = getResources().getColorStateList(R.color.gray_400)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (requireContext().getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
                    setAlarm()
                } else {
                    // 권한 거부 시 처리
                }
            }
        }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            requestPermissionLauncher.launch(intent)
        }
    }

    // 날짜 문자열을 파싱하여 년, 월, 일을 반환하는 함수
    private fun parseDate(dateString: String): Triple<Int, Int, Int> {
        val parts = dateString.split("-")
        if (parts.size == 3) {
            val year = parts[0].toInt()
            val month = parts[1].toInt() - 1 // Calendar의 month는 0부터 시작
            val dayOfMonth = parts[2].toInt()
            return Triple(year, month, dayOfMonth)
        } else {
            throw IllegalArgumentException("Invalid date format: $dateString")
        }
    }

    // 시간 문자열을 파싱하여 시, 분을 반환하는 함수
    private fun parseTime(timeString: String): Pair<Int, Int> {
        val parts = timeString.split(":")
        if (parts.size == 2) {
            val hourOfDay = parts[0].toInt()
            val minute = parts[1].toInt()
            return Pair(hourOfDay, minute)
        } else {
            throw IllegalArgumentException("Invalid time format: $timeString")
        }
    }
}
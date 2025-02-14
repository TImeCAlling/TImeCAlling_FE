package com.umc.timeCAlling.presentation.calendar

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.viewLifeCycle
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private var selectedDate: LocalDate? = null
    private lateinit var navController: NavController
    private lateinit var behavior: BottomSheetBehavior<ConstraintLayout>
    private val addScheduleViewModel : AddScheduleViewModel by activityViewModels()
    private var scheduleId : Int = 0
    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    private val adapter : DetailScheduleRVA by lazy {
        DetailScheduleRVA()
    }

    override fun initView() {
        initDetailScheduleRVA()
        initCalendar()
        initBottomSheet()
        bottomNavigationShow()

        binding.layoutDetailMembers.setOnClickListener {
            val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
            findNavController().navigate(R.id.action_calendarFragment_to_wakeupFragment, bundle)
        }

    }

    override fun initObserver() {

    }

    private fun initBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        behavior.apply {
            this.isHideable = true
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN,
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        dismissBottomSheet()
                        bottomNavigationShow()
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.layoutBottomSheet.setBackgroundResource(R.drawable.shape_bottom_sheet_expanded)
                        binding.viewCalendarHandle.visibility = View.GONE
                        binding.ivDetailClose.setImageResource(R.drawable.ic_arrow_left_detail)
                        binding.layoutDetailTopBar.layoutParams = (binding.layoutDetailTopBar.layoutParams as MarginLayoutParams).apply {
                            topMargin = requireContext().toPx(18).toInt()
                        }
                        behavior.isDraggable = false
                    }
                    else -> binding.viewBottomSheetBackground.visibility = View.VISIBLE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("slideOffset", slideOffset.toString())
            }
        })

        binding.ivDetailClose.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.viewBottomSheetBackground.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun bottomNavigationShow() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
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

    private fun showDialogBox(scheduleTitle: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_detail_schedule_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val title = dialog.findViewById<TextView>(R.id.tv_dialog_title)
        val btnYes = dialog.findViewById<TextView>(R.id.btn_dialog_yes)
        val btnNo = dialog.findViewById<TextView>(R.id.btn_dialog_no)

        title.text = "' ${scheduleTitle} '"
        btnYes.setOnClickListener {
            dialog.dismiss()
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
            addScheduleViewModel.deleteSchedule(scheduleId)
            Toast.makeText(requireContext(), "삭제는 나중에..", Toast.LENGTH_SHORT).show()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun dismissBottomSheet() {
        binding.layoutBottomSheet.setBackgroundResource(R.drawable.shape_bottom_sheet)
        binding.viewCalendarHandle.visibility = View.VISIBLE
        binding.viewBottomSheetBackground.visibility = View.GONE
        binding.ivDetailClose.setImageResource(R.drawable.ic_delete_black)
        behavior.isDraggable = true
        binding.layoutScrollView.scrollTo(0, 0)
        binding.layoutDetailTopBar.layoutParams = (binding.layoutDetailTopBar.layoutParams as MarginLayoutParams).apply {
            topMargin = 0
        }
    }

    private fun initCalendar() {
        // 오늘 날짜 구하기
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월")
        binding.tvScheduleDate.text = today.format(formatter)

        // 날짜 선택 탭
        initDatePicker()
        updateSelectionUI(0)

        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        selectedDate = LocalDate.now()
        Log.d("CalendarFragment", "getScheduleByDate 호출!!")
        scheduleViewModel.getScheduleByDate(selectedDate!!.format(formatter2))
    }

    private fun getNext7Days(): List<LocalDate> {
        val today = LocalDate.now()
        val dateList = mutableListOf<LocalDate>()
        for (i in 0 until 7) {
            dateList.add(today.plusDays(i.toLong()))
        }
        return dateList
    }

    private fun initDatePicker() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        binding.layoutCalendarDatePick.removeAllViews()

        getNext7Days().forEachIndexed { index, date ->
            val itemView = layoutInflater.inflate(R.layout.item_calendar_date, binding.layoutCalendarDatePick, false)
            val dayKo = itemView.findViewById<TextView>(R.id.tv_calendar_day_ko)
            val day = itemView.findViewById<TextView>(R.id.tv_calendar_day)

            dayKo.text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ko"))
            day.text = date.dayOfMonth.toString()

            itemView.setOnClickListener {
                selectedDate = date
                scheduleViewModel.getScheduleByDate(selectedDate!!.format(formatter))
                updateSelectionUI(index)
            }

            binding.layoutCalendarDatePick.addView(itemView)
        }
    }

    private fun updateSelectionUI(index: Int) {
        for(i in 0 until binding.layoutCalendarDatePick.childCount) {
            val child = binding.layoutCalendarDatePick.getChildAt(i)
            val day = child.findViewById<TextView>(R.id.tv_calendar_day)
            val dayKo = child.findViewById<TextView>(R.id.tv_calendar_day_ko)

            if( i == index) {
                child.setBackgroundResource(R.drawable.shape_rect_39_mint_fill)
                day.setTextColor(resources.getColor(R.color.white))
                dayKo.setTextColor(resources.getColor(R.color.white))
            } else {
                child.background = null
                day.setTextColor(resources.getColor(R.color.gray_500))
                dayKo.setTextColor(resources.getColor(R.color.gray_500))
            }
        }
    }



    private fun initDetailScheduleRVA() {
        binding.rvCalendarDetailSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@CalendarFragment.adapter
        }

        scheduleViewModel.schedules.observe(viewLifecycleOwner) { scheduleList ->
            if(scheduleList.isEmpty()) {
                binding.layoutNoSchedule.visibility = View.VISIBLE
                binding.rvCalendarDetailSchedule.visibility = View.GONE
                Log.e("CalendarFragment", "일정 없음")
            } else {
                binding.layoutNoSchedule.visibility = View.GONE
                binding.rvCalendarDetailSchedule.visibility = View.VISIBLE
                adapter.setScheduleList(scheduleList = scheduleList as ArrayList<ScheduleByDateResponseModel>)
                Log.d("CalendarFragment", "오늘 일정에 추가됐음!!!")
            }
        }

        scheduleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 로딩 중 UI 표시 (예: 프로그레스바 보이기)
                binding.progressbarCalendar.visibility = View.VISIBLE
            } else {
                // 로딩 완료 UI 표시 (예: 프로그레스바 숨기기)
                binding.progressbarCalendar.visibility = View.GONE
            }
        }

        //이 부분은 나중에 상세일정조회 api와 연결 후 수정하겠습니다
        adapter.onItemClick = { schedule ->
            bottomNavigationRemove() // 아이템 클릭 시 BottomNavigationView 숨기기
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            binding.tvDetailTitle.text = schedule.name
            binding.tvDetailTimeType.text = "오전"    //나중에 수정!!
            binding.tvDetailTime.text = schedule.meetTime
            binding.tvDetailCategory.text = schedule.categories[0].categoryName
            binding.tvDetailRepeatInfo.text = if(schedule.repeatDays == null) {"반복 안함"} else { schedule.repeatDays[0] }

            /*if (list[position].memberCount >= 4) {
                binding.layoutDetailExtraMembers.visibility = View.VISIBLE
                binding.tvDetailExtraMembers.text = "+${list[position].memberCount - 3}"
                val marginEndInDp = -12
                binding.cvDetailMemberThird.layoutParams = (binding.cvDetailMemberThird.layoutParams as MarginLayoutParams).apply {
                    marginEnd = requireContext().toPx(marginEndInDp).toInt()
                }
            } else {
                binding.layoutDetailExtraMembers.visibility = View.GONE
                binding.cvDetailMemberThird.layoutParams = (binding.cvDetailMemberThird.layoutParams as MarginLayoutParams).apply {
                    marginEnd = 0
                }
            }*/

            binding.ivDetailMore.setOnClickListener {
                val theme = ContextThemeWrapper(requireContext(), R.style.PopupMenuItemStyle)
                val popup = PopupMenu(
                    requireContext(),
                    it,
                    Gravity.CENTER,
                    0,
                    R.style.CustomPopupMenuStyle
                )
                popup.menuInflater.inflate(R.menu.popup_menu_item, popup.menu)

                try {
                    val fields = popup.javaClass.declaredFields
                    for (field in fields) {
                        if (field.name == "mPopup") {
                            field.isAccessible = true
                            val menuPopupHelper = field.get(popup)
                            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                            val setForceIconsMethod =
                                classPopupHelper.getMethod(
                                    "setForceShowIcon",
                                    Boolean::class.javaPrimitiveType
                                )
                            setForceIconsMethod.invoke(menuPopupHelper, true)
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.popup_edit -> {
                            val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
                            navController.navigate(R.id.action_calendarFragment_to_addScheduleTab, bundle)
                            Toast.makeText(requireContext(), "수정하기", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.popup_delete -> {
                            showDialogBox(schedule.name)
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )
}
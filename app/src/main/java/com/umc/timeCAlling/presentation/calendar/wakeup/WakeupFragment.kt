package com.umc.timeCAlling.presentation.calendar.wakeup

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.DialogWakeupBinding
import com.umc.timeCAlling.databinding.DialogWakeupShareBinding
import com.umc.timeCAlling.databinding.FragmentWakeupBinding
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.calendar.ScheduleViewModel
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakeupFragment: BaseFragment<FragmentWakeupBinding>(R.layout.fragment_wakeup) {

    private lateinit var wakeupPeopleRVA: WakeupPeopleRVA
    private val viewModel: ScheduleViewModel by activityViewModels()
    private val wakeupAlarmViewModel : WakeupViewModel by activityViewModels()

    override fun initObserver() {
        viewModel.scheduleUsers.observe(viewLifecycleOwner) { users ->
            if (users.isNullOrEmpty()) {
                wakeupPeopleRVA.submitList(emptyList())
            } else {
                wakeupPeopleRVA.submitList(users)
            }
        }
    }

    override fun initView() {
        viewModel.detailSchedule.observe(viewLifecycleOwner) { schedule ->

            Log.d("DetailSchedule", schedule.toString())

            initDetailScheduleBottomSheet(schedule)
        }

        wakeupPeopleRVA = WakeupPeopleRVA(
            wakeupAlarmViewModel,viewLifecycleOwner
        )
        wakeupPeopleRVA.onItemClick = { user ->
            // 공유 스케줄 여부 확인
            if (viewModel.isSharedSchedule) {
                // 공유 스케줄인 경우, 클릭 이벤트 처리
                Toast.makeText(requireContext(), "공유 스케줄 클릭", Toast.LENGTH_SHORT).show()
            } else {
                // 공유 스케줄이 아닌 경우, 토스트 메시지 표시
                Toast.makeText(requireContext(), "공유되지 않은 일정입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        bottomNavigationShow()

        binding.ivWakeupShare.setOnSingleClickListener {
            showWakeupShareDialog()
        }

        binding.ivWakeupBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }

        binding.rvWakeupPeople.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWakeupPeople.adapter = wakeupPeopleRVA

        val scheduleId = arguments?.getInt("scheduleId") ?: -1
        if (scheduleId != -1) {
            viewModel.getScheduleUsers(scheduleId)
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

    private fun showWakeupShareDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_wakeup_share, null)
        val dialogBinding = DialogWakeupShareBinding.bind(dialogView)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val layoutParams = dialog.window?.attributes
        layoutParams?.dimAmount = 0.8f
        dialog.window?.attributes = layoutParams

        dialogBinding.tvDialogSuccess.setOnSingleClickListener {
            dialog.dismiss()
        }
    }


    private fun initDetailScheduleBottomSheet(schedule: DetailScheduleResponseModel) {
        val meetTime = schedule.meetTime
        wakeupAlarmViewModel.setScheduledDate(meetTime)
        wakeupAlarmViewModel.setSharedId(schedule.shareId?:"")
        val parsedTime = parseTimeString(meetTime)
        if (parsedTime != null) {
            val (hours, minutes) = parsedTime
            val formattedHours = if (hours == 0) 12 else if (hours > 12) hours - 12 else hours
            val formattedMinutes = String.format("%02d", minutes)

            binding.tvWakeupAp.text = if (hours < 12) "오전" else "오후"
            binding.tvWakeupTime.text =
                "${String.format("%02d", formattedHours)}:${formattedMinutes}"
        }

        binding.tvWakeupScheduleName.text = schedule.name
        binding.tvWakeupDate.text = schedule.meetDate

        if (schedule.repeatDays != null && schedule.start != null && schedule.end != null) {
            binding.tvWakeupRepeat.text = repeatDaysToKo(schedule.repeatDays)
            binding.tvWakeupRepeatStart.text = schedule.start
            binding.tvWakeupRepeatEnd.text = schedule.end
            binding.layoutWakeupRepeat.visibility = View.VISIBLE
        } else {
            binding.tvWakeupRepeat.text = "반복 안함"
            binding.layoutWakeupRepeat.visibility = View.GONE
        }
    }

    private fun parseTimeString(timeString: String): Pair<Int, Int>? {
        // 입력 문자열이 유효한 형식인지 확인
        if (!timeString.matches(Regex("\\d{2}:\\d{2}"))) {
            return null
        }
        val parts = timeString.split(":")
        if (parts.size != 2) {
            return null
        }

        val hours = parts[0].toIntOrNull()
        val minutes = parts[1].toIntOrNull()

        // 변환 실패 시 null 반환
        if (hours == null || minutes == null) {
            return null
        }

        return Pair(hours, minutes)
    }

    private fun dayConverter(day: String): String {
        when(day) {
            "MONDAY" -> return "월"
            "TUESDAY" -> return "화"
            "WEDNESDAY" -> return "수"
            "THURSDAY" -> return "목"
            "FRIDAY" -> return "금"
            "SATURDAY" -> return "토"
            "SUNDAY" -> return "일"
            else -> return ""
        }
    }

    private fun repeatDaysToKo(repeatDays: List<String>): String {
        var koDays = ""
        if(repeatDays.size == 7) koDays = "매일"
        else {
            koDays = repeatDays.map { dayConverter(it) }.joinToString(", ")
        }
        return koDays + " 반복"
    }

    private fun freeTimeConverter(freeTime: String) : String {
        return when(freeTime) {
            "TIGHT" -> "딱딱"
            "RELAXED" -> "여유"
            "PLENTY" -> "넉넉"
            else -> ""
        }
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )
}

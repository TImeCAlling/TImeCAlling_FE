package com.umc.timeCAlling.presentation.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.text
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.TopSheetBehavior
import com.umc.timeCAlling.databinding.DialogScheduleShareBinding
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.calendar.ScheduleViewModel
import com.umc.timeCAlling.presentation.home.adapter.LastScheduleRVA
import com.umc.timeCAlling.presentation.home.adapter.TodayScheduleRVA
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.LocalTime

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var navController: NavController
    private lateinit var behavior: TopSheetBehavior<View>
    private val viewModel: HomeViewModel by activityViewModels()
    private val scheduleViewModel : AddScheduleViewModel by activityViewModels()
    private val lastScheduleViewModel: ScheduleViewModel by activityViewModels()
    private var dialog: androidx.appcompat.app.AlertDialog? = null

    override fun initView() {
        viewModel.clearAll()
        arguments?.let {
            val scheduleId = it.getInt("scheduleId", -1)
            if (scheduleId != -1) {
                scheduleViewModel.sharedSchedule(scheduleId)

                val dialogBinding: DialogScheduleShareBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(requireContext()),
                    R.layout.dialog_schedule_share,
                    null,
                    false
                )

                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setView(dialogBinding.root)
                    .setCancelable(false)
                    .create()

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()

                val layoutParams = dialog.window?.attributes
                layoutParams?.dimAmount = 0.8f
                dialog.window?.attributes = layoutParams

                scheduleViewModel.sharedScheduleNickname.observe(viewLifecycleOwner, Observer { nickname ->
                    scheduleViewModel.scheduleName.observe(viewLifecycleOwner, Observer { scheduleName ->
                        if (nickname != null && scheduleName != null) {
                            dialogBinding.nickname = nickname
                            dialogBinding.scheduleName = scheduleName
                            dialog?.show()
                        }
                    })
                })

                dialogBinding.tvDialogSuccess.setOnSingleClickListener {
                    val bundle = Bundle().apply {
                        putInt("scheduleId", scheduleId)
                    }
                    scheduleViewModel.setMode("shared")
                    findNavController().navigate(R.id.action_homeFragment_to_addScheduleTab, bundle)
                    dialog.dismiss()
                }
            }
        }
        binding.layoutHomeTodayScheduleDetail.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
        binding.ivHomeMypage.setOnSingleClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mypageTab)
        }
        initTodayDate()
        initLastScheduleRV()
        initTodayScheduleRV()
        initTopSheet()
        bottomNavigationShow()
        setProgressBar()
    }
    override fun initObserver() {
    }

    private fun initTodayDate() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("M월 d일")
        binding.tvHomeTodayDate.text = today.format(formatter)
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
            }
        })

        binding.apply {
            viewHomeTopsheetBackground.setOnClickListener {
                behavior.state = TopSheetBehavior.STATE_COLLAPSED
            }
            ivPreClose.setOnClickListener {
                behavior.state = TopSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun showTopSheet(scheduleId: Int) {

        viewModel.getScheduleStatus(scheduleId)
        lastScheduleViewModel.getScheduleUsers(scheduleId)
        lastScheduleViewModel.getUser()
        lastScheduleViewModel.user.observe(viewLifecycleOwner) {user ->
            binding.layoutPreMembers.removeAllViews()
            val inflater = layoutInflater
            val profile: View = inflater.inflate(R.layout.item_detail_schedule_member, binding.layoutPreMembers, false)
            val profileImage = profile.findViewById<ImageView>(R.id.img_detail_schedule_member)

            Glide.with(requireContext())
                .load(user.profileImage)
                .into(profileImage)

            binding.layoutPreMembers.addView(profile)
            lastScheduleViewModel.scheduleUsers.observe(viewLifecycleOwner) { scheduleUsers ->
                if (scheduleUsers.isEmpty()) {
                    //do nothing yet
                } else {
                    //do nothing yet
                }
            }
        }

        viewModel.scheduleStatus.observe(viewLifecycleOwner) { status ->
            binding.apply {
                val meetTime = status.meetTime
                val parsedTime = parseTimeString(meetTime)
                if (parsedTime != null) {
                    val (hours, minutes, seconds) = parsedTime
                    val formattedHours = if (hours == 0) 12 else if (hours > 12) hours - 12 else hours
                    val formattedMinutes = String.format("%02d", minutes)

                    tvPreTimeType.text = if (hours < 12) "오전" else "오후"
                    val formattedTime = "${String.format("%02d", formattedHours)}:${formattedMinutes}"
                    tvPreTime.text = formattedTime
                }

                tvPreTitle.text = status.name

                val totalTime = status.totalTime
                val parsedTotalTime = parseTimeString(totalTime)
                if(parsedTotalTime != null) {
                    val (hours, minutes, seconds) = parsedTotalTime
                    tvPreTotalTime.text = if(hours == 0) "${minutes}분" else "${hours}시간 ${minutes}분"
                }
                tvPreLeftTime.text = if(status.leftTime > 60) "${status.leftTime / 60}시간 ${status.leftTime % 60}분" else "${status.leftTime}분"

                val progress = calculatePreparationProgress(meetTime, totalTime)
                progressbar.progress = progress
                tvProgressPercent.text = progress.toString()
            }
        }


        behavior.state = TopSheetBehavior.STATE_EXPANDED
        binding.viewHomeTopsheetBackground.visibility = View.VISIBLE
        binding.viewHomeTopsheetBackground.setOnSingleClickListener {
            behavior.state = TopSheetBehavior.STATE_COLLAPSED
            binding.viewHomeTopsheetBackground.visibility = View.GONE
        }
        bottomNavigationRemove()
    }

    private fun dismissTopSheet() {
        binding.apply {
            viewHomeTopsheetBackground.visibility = View.GONE
            layoutTopsheetScroll.scrollTo(0,0)
        }
        bottomNavigationShow()
    }

    private fun initLastScheduleRV() {
        viewModel.loadItems()
        val adapter = LastScheduleRVA()
        binding.rvHomeLastSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }

        viewModel.checklists.observe(viewLifecycleOwner) { list ->
            Log.d("HomeFragment", "Checklists: $list")

            if (list.isEmpty()) {
                binding.rvHomeLastSchedule.visibility = View.GONE
                binding.tvHomeNoLastSchedule.visibility = View.VISIBLE
            } else {
                val lastSchedules = mutableListOf<DetailScheduleResponseModel>()
                var receivedCount = 0

                list.forEach { checklistId ->
                    lastScheduleViewModel.getDetailSchedule(checklistId)
                }

                // 한 번만 observe 해서 모든 데이터를 받았을 때 RecyclerView 갱신
                lastScheduleViewModel.detailSchedule.observe(viewLifecycleOwner) { lastSchedule ->
                    if (!lastSchedules.contains(lastSchedule)) { // 중복 추가 방지
                        lastSchedules.add(lastSchedule)
                        receivedCount++

                        if (receivedCount == list.size) {
                            val sortedLastSchedules = lastSchedules.sortedBy{it.meetTime}
                            adapter.setLastScheduleList(scheduleList = sortedLastSchedules)
                            binding.rvHomeLastSchedule.visibility = View.VISIBLE
                            binding.tvHomeNoLastSchedule.visibility = View.GONE
                        }
                    }
                }
            }
        }

        adapter.onItemClick = { schedule ->
            val action = HomeFragmentDirections.actionHomeFragmentToCheckListFragment(scheduleIndex = schedule.scheduleId)
            findNavController().navigate(action)
        }
    }

    private fun initTodayScheduleRV() {
        viewModel.getTodaySchedules()
        val adapter = TodayScheduleRVA()
        binding.rvHomeTodaySchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        viewModel.todaySchedules.observe(viewLifecycleOwner) { scheduleList ->
            if(scheduleList.isEmpty()) {
                binding.rvHomeTodaySchedule.visibility = View.GONE
                binding.layoutHomeNoTodaySchedule.visibility = View.VISIBLE
            } else {
                binding.rvHomeTodaySchedule.visibility = View.VISIBLE
                binding.layoutHomeNoTodaySchedule.visibility = View.GONE
                adapter.setScheduleList(scheduleList = scheduleList)
            }
        }

        adapter.onItemClick = { schedule ->
            showTopSheet(schedule.scheduleId)
        }
    }

    private fun setProgressBar() {
        // viewHomeProgressBarBackground가 레이아웃된 후에 실행
        binding.viewHomeProgressBarBackground.post {
            val maxWidth = binding.viewHomeProgressBarBackground.width
            viewModel.getSuccessRate()
            viewModel.successRate.observe(viewLifecycleOwner) { response ->
                binding.apply {
                    tvHomeProgress.text = "${response.successRate.toInt()}%"
                    viewHomeProgressBarForeground.layoutParams = (viewHomeProgressBarForeground.layoutParams as ViewGroup.LayoutParams).apply {
                        this.width = (maxWidth * response.successRate / 100).toInt()
                        if(this.width < requireContext().toPx(20).toInt()) this.width = requireContext().toPx(20).toInt()
                    }
                }
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

    private fun parseTimeString(timeString: String): Triple<Int, Int, Int>? {
        // 입력 문자열이 유효한 형식인지 확인
        if (!timeString.matches(Regex("\\d{2}:\\d{2}:\\d{2}"))) {
            return null
        }
        val parts = timeString.split(":")
        if (parts.size != 3) {
            return null
        }

        val hours = parts[0].toIntOrNull()
        val minutes = parts[1].toIntOrNull()
        val seconds = parts[2].toIntOrNull()

        // 변환 실패 시 null 반환
        if (hours == null || minutes == null || seconds == null) {
            return null
        }

        return Triple(hours, minutes, seconds)
    }

    private fun calculatePreparationProgress(meetTime: String, totalTime: String): Int {

        //1. LocalTime으로 변환
        val meetTimeLocalTime = LocalTime.parse(meetTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
        val totalTimeLocalTime = LocalTime.parse(totalTime, DateTimeFormatter.ofPattern("HH:mm:ss"))

        // 2. 준비 시작 시간 계산
        val preparationStartTime = meetTimeLocalTime.minusHours(totalTimeLocalTime.hour.toLong())
            .minusMinutes(totalTimeLocalTime.minute.toLong())
            .minusSeconds(totalTimeLocalTime.second.toLong())

        // 3. 현재 시간 가져오기
        val now = LocalTime.now()

        // 6. 예외 처리
        if (now.isAfter(meetTimeLocalTime)) {
            return 100 // 현재 시간이 meetTime 이후일 경우, 진행도는 100%
        }
        if(now.isBefore(preparationStartTime)) {
            return 0 // 현재 시간이 준비 시작 시간 이전일 경우, 진행도는 0%
        }

        // 4. 시간 차이 계산
        val durationToNow = Duration.between(preparationStartTime, now)
        val durationToMeetTime = Duration.between(preparationStartTime, meetTimeLocalTime)

        // 5. 진행도 계산
        val progress = (durationToNow.toMillis().toDouble() / durationToMeetTime.toMillis() * 100).toInt()

        return progress
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationSearchFragment", "Location permission already granted")
        } else {
            Log.d("LocationSearchFragment", "Requesting location permission")
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationSearchFragment", "Location permission granted by user")
            } else {
                Log.d("LocationSearchFragment", "Location permission denied by user")
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

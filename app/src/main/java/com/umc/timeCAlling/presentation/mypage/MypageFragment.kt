package com.umc.timeCAlling.presentation.mypage

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageBinding
import com.umc.timeCAlling.presentation.calendar.ScheduleViewModel
import com.umc.timeCAlling.presentation.home.HomeViewModel
import com.umc.timeCAlling.util.network.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageFragment: BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    private val spf by lazy { requireActivity().getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE) }
    private val myprofileViewModel: MyprofileViewModel by viewModels()
    private var nickname: String = ""

    override fun initView() {
        setClickListener()
        bottomNavigationShow()

        myprofileViewModel.getUser()
        observeViewModel()
        initSuccessRate()
        initProfile()
    }

    override fun initObserver() {
        mypageProfileObserve()
    }

    private fun setClickListener() {
        binding.apply {
            clMypageSetting.setOnClickListener {
                navigateToMyprofileFragment() // 내 프로필
            }
            layoutMypageAlarmlist.setOnClickListener {
                //findNavController().navigate(R.id.action_mypageFragment_to_alarmlistFragment)
            }
            layoutMypageCategory.setOnClickListener {
                findNavController().navigate(R.id.action_mypageFragment_to_categoryEditFragment)
            }
            layoutMypageSoundInfo.setOnClickListener {
                //navigateToMypageVoiceFragment()
            }
            layoutMypageTerms.setOnClickListener {
                //이용약관
            }
            ivMypageBack.setOnClickListener { findNavController().navigate(R.id.action_global_homeFragment) }
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

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myprofileViewModel.userInfo.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            Log.d("MyprofileFragment", "Loading user data")
                        }
                        is UiState.Success -> {
                            val user = state.data
                            Log.d("MyprofileFragment", "UI 업데이트: $user")

                            // 사용자 정보 업데이트
                            nickname = user.nickname

                            // UI 업데이트
                            binding.tvMypageName.text = user.nickname

                            // 프로필 이미지 업데이트
                            if (user.profileImage.isNullOrEmpty()) {
                                binding.ivMypageProfile.setImageResource(R.drawable.shape_rect_999_trans_fill)
                                binding.ivMypageProfile.visibility = View.VISIBLE
                            } else {
                                Glide.with(this@MypageFragment)
                                    .load(user.profileImage)
                                    .placeholder(R.drawable.shape_rect_999_white_fill)
                                    .error(R.drawable.ic_profile_default_default)
                                    .into(binding.ivMypageProfile)
                            }
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "사용자 정보 로드 실패.", Toast.LENGTH_SHORT).show()
                            Log.e("MyprofileFragment", "사용자 정보 로드 실패")
                        }
                        UiState.Empty -> Log.d("MyprofileFragment", "사용자 정보 없음")
                    }
                }
            }
        }
    }

    private fun navigateToMyprofileFragment() {
        findNavController().navigate(R.id.action_mypageFragment_to_myprofileFragment)
    }

    private fun navigateToMypageVoiceFragment() {
        findNavController().navigate(R.id.action_mypageFragment_to_mypageVoiceFragment)
    }

    private fun initSuccessRate() {
        binding.viewMypageProgressBackground.post {
            val maxWidth = binding.viewMypageProgressBackground.width
            homeViewModel.getSuccessRate()
            homeViewModel.successRate.observe(viewLifecycleOwner) { response ->
                binding.apply {
                    val successRate = response.successRate.toInt()
                    val nearestTen = roundUpToNearestTen(successRate)
                    tvMypageCurrentProgress.text = "약속 성공률 ${successRate}%\n다음 목표 ${if(successRate == 100) 100 else nearestTen}%, 함께 도전해요!"
                    tvMypageProgressPercent.text = "${response.successRate.toInt()}%"
                    viewMypageProgressForeground.layoutParams = (viewMypageProgressForeground.layoutParams as ViewGroup.LayoutParams).apply {
                        this.width = (maxWidth * response.successRate / 100).toInt()
                        if(this.width < requireContext().toPx(20).toInt()) this.width = requireContext().toPx(20).toInt()
                    }
                    tvMypageScheduleSuccess.text = "${response.success}개"
                    tvMypageScheduleFailure.text = "${response.failed}개"
                }
            }
        }
    }

    private fun roundUpToNearestTen(number: Int): Int {
        if (number < 0) {
            throw IllegalArgumentException("Number must be non-negative")
        }
        return (number / 10 + 1) * 10
    }

    private fun initProfile() {
        scheduleViewModel.getUser()
    }

    private fun mypageProfileObserve() {
        scheduleViewModel.user.observe(viewLifecycleOwner) { response ->
            binding.apply {
                binding.tvMypageName.text = response.nickname + "님"
                spf.edit().apply{
                    putString("nickName", response.nickname)
                    apply()
                }
                Glide.with(requireContext())
                    .load(response.profileImage)
                    .into(ivMypageProfile)
            }
        }
    }

    fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

}
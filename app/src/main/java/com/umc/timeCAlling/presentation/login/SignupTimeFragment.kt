package com.umc.timeCAlling.presentation.login

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupTimeBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.SignupTimeAdapter
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupTimeFragment : BaseFragment<FragmentSignupTimeBinding>(R.layout.fragment_signup_time) {

    private val signupViewModel: SignupViewModel by activityViewModels()

    private val timeOptions = listOf(" ", " ", "15분", "30분", "45분", "60분", "90분+", " ", " ")
    private var previousCenterPosition: Int? = null

    override fun initView() {
        setupRecyclerView()
        setClickListener()
    }

    override fun initObserver() {
        // 필요한 경우 옵저버 추가 가능
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = SignupTimeAdapter(timeOptions)

        binding.rvSignupTimeRecyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter

            // LinearSnapHelper 설정
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)

            // 초기화 시 첫 번째 아이템 선택 처리
            post {
                val initialPosition = 2
                adapter.updateItemColor(initialPosition, true)
                previousCenterPosition = initialPosition
            }

            // 중앙 아이템 감지 및 색상 변경
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(layoutManager)
                        val centerPosition = centerView?.let { layoutManager.getPosition(it) }
                        if (centerPosition != null && centerPosition != previousCenterPosition) {
                            // 중앙 아이템 색상 변경
                            adapter.updateItemColor(centerPosition, true)
                            previousCenterPosition?.let { adapter.updateItemColor(it, false) }
                            previousCenterPosition = centerPosition
                        }
                    }
                }
            })
        }
    }

    private fun getSelectedTime(): Int {
        // 선택된 값 가져오기 (예: "15분", "30분", "60분")
        val selectedTime = previousCenterPosition?.let { timeOptions[it] } ?: "0"

        // 앞 두 글자(숫자 부분)만 Int로 변환
        return selectedTime.trim().take(2).toIntOrNull() ?: 0
    }

    private fun setClickListener() {
        // 다음 버튼 클릭 리스너 설정
        binding.tvSignupTimeNext.setOnClickListener {
            val selectedPrepTime = getSelectedTime()

            // ViewModel에 준비 시간 저장
            signupViewModel.setAvgPrepTime(selectedPrepTime)
            Log.d("SignupTimeFragment", "평균 준비 시간: $selectedPrepTime")

            // 다음 화면으로 이동
            navigateToSignupSpareFragment()
        }
    }

    private fun navigateToSignupSpareFragment() {
        // Navigation을 통해 다음 Fragment로 이동
        findNavController().navigate(R.id.action_signupTimeFragment_to_signupSpareFragment)
    }
}
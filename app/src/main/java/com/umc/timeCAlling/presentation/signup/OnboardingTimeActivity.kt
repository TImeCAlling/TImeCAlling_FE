package com.umc.timeCAlling.presentation.signup

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTimeBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import com.umc.timeCAlling.presentation.signup.adapter.SignupTimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTimeActivity : BaseActivity<ActivityOnboardingTimeBinding>(R.layout.activity_onboarding_time) {

    private val timeOptions = listOf(" ", " ", "15분", "30분", "45분", "60분", "90분+", " ", " ")
    private var previousCenterPosition: Int? = null

    override fun initView() {
        setupRecyclerView()
        setClickListener()
    }

    override fun initObserver() {}

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = SignupTimeAdapter(timeOptions)

        binding.rvOnboardingTimeRecyclerView.apply {
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

    private fun setClickListener() {
        binding.tvOnboardingTimeNext.setOnClickListener {
            navigateToOnboardingSpareActivity()
        }
    }

    private fun navigateToOnboardingSpareActivity() {
        val intent = Intent(this, OnboardingSpareActivity::class.java)
        startActivity(intent)
    }
}
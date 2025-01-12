package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTimeBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import com.umc.timeCAlling.presentation.onboarding.adapter.OnboardingTimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTimeActivity : BaseActivity<ActivityOnboardingTimeBinding>(R.layout.activity_onboarding_time) {

    private val timeOptions = listOf(" ", " ", "15분", "30분", "45분", "60분", "90분+", " ", " ")

    override fun initView() {
        setupRecyclerView()
        setClickListener()
    }

    override fun initObserver() {}

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = OnboardingTimeAdapter(timeOptions)

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter


            // LinearSnapHelper를 사용하여 중앙 정렬 활성화
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
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
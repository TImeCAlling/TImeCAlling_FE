package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTimeBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTimeActivity : BaseActivity<ActivityOnboardingTimeBinding>(R.layout.activity_onboarding_time) {

    // private val viewModel: OnboardingTimeViewModel by viewModels() // ViewModel 연결

    override fun initView() {

        setClickListener()
    }

    override fun initObserver() {

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
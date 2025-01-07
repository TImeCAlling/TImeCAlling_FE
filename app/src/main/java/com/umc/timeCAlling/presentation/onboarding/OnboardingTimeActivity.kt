package com.umc.timeCAlling.presentation.onboarding

import androidx.activity.viewModels
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTimeBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTimeActivity : BaseActivity<ActivityOnboardingTimeBinding>(R.layout.activity_onboarding_time) {

    private val viewModel: OnboardingTimeViewModel by viewModels() // ViewModel 연결

    override fun initView() {

    }

    override fun initObserver() {

    }
}
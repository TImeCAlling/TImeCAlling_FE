package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingPhotoBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingPhotoActivity : BaseActivity<ActivityOnboardingPhotoBinding>(R.layout.activity_onboarding_photo) {

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.tvOnboardingPhotoNext.setOnClickListener {
            navigateToOnboardingNameActivity()
        }
    }

    private fun navigateToOnboardingNameActivity() {
        val intent = Intent(this, OnboardingNameActivity::class.java)
        startActivity(intent)
    }
}
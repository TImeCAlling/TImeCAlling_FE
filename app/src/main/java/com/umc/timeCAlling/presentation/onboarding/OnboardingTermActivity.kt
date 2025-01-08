package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTermBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTermActivity : BaseActivity<ActivityOnboardingTermBinding>(R.layout.activity_onboarding_term) {

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.tvOnboardingTermNext.setOnClickListener {
            navigateToOnboardingPhotoActivity()
        }
    }

        private fun navigateToOnboardingPhotoActivity() {
        val intent = Intent(this, OnboardingPhotoActivity::class.java)
        startActivity(intent)
    }
}
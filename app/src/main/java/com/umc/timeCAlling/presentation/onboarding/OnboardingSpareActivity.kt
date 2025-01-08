package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingSpareBinding
import com.umc.timeCAlling.presentation.MainActivity
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSpareActivity : BaseActivity<ActivityOnboardingSpareBinding>(R.layout.activity_onboarding_spare) {

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.tvOnboardingSpareNext.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
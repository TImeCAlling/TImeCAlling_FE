package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun initView() {
        setClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.ivOnboardingKakaoLogin.setOnClickListener {

            navigateToOnboardingTermActivity() // kakao login 구현 후 지우기
        }
    }

    private fun navigateToOnboardingTermActivity() {
        val intent = Intent(this, OnboardingTermActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.umc.timeCAlling.presentation

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivitySplashBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import com.umc.timeCAlling.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun initView() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            // checkJwt()

            navigateToOnboardingActivity() // checkjwt() 작성 후 지우기

        }, 1000)
    }

    override fun initObserver() {

        /*
        viewModel.checkJwtResult.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {
                    Log.d("login", "Jwt 확인 error : ${result.exception}")
                }
                is NetworkResult.Fail -> {
                    Log.d("login", "Jwt 확인 실패 : ${result.statusCode}")
                }
                is NetworkResult.Success -> {
                    Log.d("login", "Jwt 확인 성공")
                    navigateToMainActivity()
                }
            }
        }
        */
    }
    /*
    private fun checkJwt() {
        val userId = UserPreferences(this).getUserId()

        if (userId != null) {
            Log.d("login", "JWT 발견 $userId")
            Log.d("login", "refresh : ${UserPreferences(this).getUserIdRefresh()}")
            viewModel.checkJwt()
        } else {
            Log.d("login", "JWT 없음")
            navigateToOnboardingActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    */
    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.umc.timeCAlling.presentation

import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivitySplashBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun initView() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            navigateToSignupFragment()
        }, 1000)
    }

    override fun initObserver() {
    }

    private fun navigateToSignupFragment() {
        // NavController를 통해 signupFragment로 이동
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.signupFragment)
    }
}
package com.umc.timeCAlling.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityMainBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun initView() {
        initNavigator()
        initClickListener() // 클릭 이벤트 초기화
        handleDeepLink(intent) // 딥링크 처리
        requestNotificationPermission()
    }

    override fun initObserver() {

    }

    private fun initNavigator() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
    }

    private fun initClickListener() {
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addScheduleFragmentTab -> {
                    // 일정 추가 프래그먼트로 이동
                    navController.navigate(R.id.addScheduleTab)
                    true
                }
                else -> {
                    // 기본 네비게이션 처리
                    navController.navigate(menuItem.itemId)
                    true
                }
            }
        }
    }

    private fun handleDeepLink(intent: Intent?) {
        val appLinkData: Uri? = intent?.data
        if (appLinkData != null) {
            val scheduleId = appLinkData.lastPathSegment?.toIntOrNull()
            if (scheduleId != null) {
                Log.d("MainActivity", "scheduleId: $scheduleId")
                val bundle = Bundle().apply { putInt("scheduleId", scheduleId) }
                val navOptions = navOptions {
                    launchSingleTop = true
                }
                navController.navigate(R.id.homeFragment, bundle, navOptions)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("MainActivity", "Notification permission granted")
            } else {
                Log.d("MainActivity", "Notification permission denied")
            }
        }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
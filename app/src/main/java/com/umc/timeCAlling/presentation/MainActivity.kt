package com.umc.timeCAlling.presentation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
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
}
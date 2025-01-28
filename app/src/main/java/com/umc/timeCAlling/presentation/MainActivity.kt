package com.umc.timeCAlling.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityMainBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLoggedIn = true // 항상 로그인된 상태로 초기화

        // NavController 초기화
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 로그인 여부에 따라 시작 Destination 변경
        val navGraph = navController.navInflater.inflate(R.navigation.main_graph) // Navigation Graph ID
        navGraph.setStartDestination(R.id.homeFragment) // setStartDestination() 메서드 사용
        navController.graph = navGraph // 변경된 Navigation Graph 적용
    }

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

        // 딥 링크 데이터 가져오기
        val data: Uri? = intent?.data

        // 딥 링크 데이터 처리
        if (data != null && data.pathSegments.contains("schedules")) {
            val scheduleIdString = data.lastPathSegment
            val scheduleId = scheduleIdString?.toIntOrNull()

            if (scheduleId != null) {
                navController.navigate(R.id.action_global_addScheduleFragment, bundleOf("scheduleId" to scheduleId))
                startActivity(intent)
            }
        }
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
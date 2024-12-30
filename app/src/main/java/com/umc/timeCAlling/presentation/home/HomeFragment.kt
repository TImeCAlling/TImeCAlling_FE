package com.umc.timeCAlling.presentation.home

import androidx.navigation.NavController
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var navController: NavController
    override fun initView() {

    }

    override fun initObserver() {

    }
}
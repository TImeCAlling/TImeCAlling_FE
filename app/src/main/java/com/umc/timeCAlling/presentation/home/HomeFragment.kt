package com.umc.timeCAlling.presentation.home

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentHomeBinding
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var navController: NavController
    override fun initView() {
        binding.ivGoToAddSchedule.setOnSingleClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addScheduleTab)
        }
    }

    override fun initObserver() {

    }
}
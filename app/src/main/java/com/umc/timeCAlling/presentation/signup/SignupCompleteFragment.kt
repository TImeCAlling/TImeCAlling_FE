package com.umc.timeCAlling.presentation.signup

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupCompleteBinding
import com.umc.timeCAlling.databinding.FragmentSignupTimeBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.signup.adapter.SignupTimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupCompleteFragment : BaseFragment<FragmentSignupCompleteBinding>(R.layout.fragment_signup_complete) {

    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }


    private fun setClickListener() {
        // 다음 버튼 클릭 리스너 설정
        binding.tvOnboardingNameNext.setOnClickListener {
            navigateToHomeFragment()
        }
    }

    private fun navigateToHomeFragment() {
        // Navigation을 통해 다음 Fragment로 이동
        findNavController().navigate(R.id.action_signupCompleteFragment_to_homeFragment)
    }
}
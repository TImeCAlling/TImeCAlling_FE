package com.umc.timeCAlling.presentation.login

import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupCompleteBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
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
        binding.tvSignupNameNext.setOnClickListener {
            navigateToHomeFragment()
        }
    }

    private fun navigateToHomeFragment() {
        // Navigation을 통해 다음 Fragment로 이동
        findNavController().navigate(R.id.action_signupCompleteFragment_to_homeFragment)
    }
}
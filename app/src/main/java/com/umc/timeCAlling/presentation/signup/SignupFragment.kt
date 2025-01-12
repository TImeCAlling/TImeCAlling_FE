package com.umc.timeCAlling.presentation.signup

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {

    private val viewModel: SignupViewModel by viewModels()


    override fun initView() {
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        binding.ivOnboardingKakaoLogin.setOnClickListener {

            navigateToSignupTermFragment() // kakao login 구현 후 지우기
        }

        binding.ivNavigateHome.setOnClickListener {

            navigateToHomeFragment() // 나중에 지우기
        }
    }

    private fun navigateToSignupTermFragment() {
        findNavController().navigate(R.id.action_signupFragment_to_signupTermFragment)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_signupSpareFragment_to_homeFragment)
    }
}
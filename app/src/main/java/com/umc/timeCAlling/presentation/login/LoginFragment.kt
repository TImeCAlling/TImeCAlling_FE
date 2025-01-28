package com.umc.timeCAlling.presentation.login

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLoginBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.LoginViewModel
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()
    private val signupViewModel: SignupViewModel by activityViewModels()

    override fun initView() {
        Log.d("LoginFragment", "initView() 호출됨")
        setClickListener()
        bottomNavigationRemove()
    }

    override fun initObserver() {
        Log.d("LoginFragment", "initObserver() 호출됨")
        loginViewModel.loginResult.observe(viewLifecycleOwner, { loginResponse ->
            if (loginResponse != null) {
                Log.d("LoginFragment", "로그인 성공: $loginResponse")
                signupViewModel.setKakaoAccessToken(loginResponse.accessToken)
                navigateToHomeFragment()
            } else {
                Log.d("LoginFragment", "로그인 실패")
                navigateToSignupTermFragment()
            }
        })
    }

    private fun setClickListener() {
        binding.ivLoginKakaoLogin.setOnClickListener {
            loginWithKakao()
        }

        binding.ivNavigateHome.setOnClickListener {
            navigateToHomeFragment()
            bottomNavigationShow()
        }
    }

    private fun loginWithKakao() {
        Log.d("LoginFragment", "loginWithKakao() 호출됨")
        loginViewModel.loginWithKakao(requireContext())
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun navigateToSignupTermFragment() {
        Log.d("LoginFragment", "navigateToSignupTermFragment() 호출됨")
        findNavController().navigate(R.id.action_loginFragment_to_signupTermFragment)
    }

    private fun navigateToHomeFragment() {
        Log.d("LoginFragment", "navigateToHomeFragment() 호출됨")
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun bottomNavigationShow() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
    }
}
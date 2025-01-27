package com.umc.timeCAlling.presentation.login

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLoginBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        Log.d("LoginFragment", "initView() 호출됨")
        setClickListener()
        bottomNavigationRemove()
    }

    override fun initObserver() {
        Log.d("LoginFragment", "initObserver() 호출됨")
        viewModel.loginResult.observe(viewLifecycleOwner, { loginResponse ->
            if (loginResponse != null) {
                Log.d("LoginFragment", "로그인 성공: $loginResponse")
                navigateToHomeFragment()
            } else {
                Log.d("LoginFragment", "로그인 실패")
                navigateToSignupTermFragment()
            }
        })
    }

    private fun setClickListener() {
        Log.d("LoginFragment", "setClickListener() 호출됨")
        binding.ivLoginKakaoLogin.setOnClickListener {
            Log.d("LoginFragment", "카카오 로그인 버튼 클릭됨")
            loginWithKakao()
        }

        binding.ivNavigateHome.setOnClickListener {
            Log.d("LoginFragment", "홈 화면으로 이동 버튼 클릭됨")
            navigateToHomeFragment()
            bottomNavigationShow()
        }
    }

    private fun loginWithKakao() {
        Log.d("LoginFragment", "loginWithKakao() 호출됨")
        val context = requireContext()

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            Log.d("LoginFragment", "카카오톡으로 로그인 가능")
            loginWithKakaoTalk()
        } else {
            Log.d("LoginFragment", "카카오 계정으로 로그인 시도")
            loginWithKakaoAccount()
        }
    }

    private fun loginWithKakaoTalk() {
        Log.d("LoginFragment", "loginWithKakaoTalk() 호출됨")
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null) {
                Log.e("LoginFragment", "카카오톡으로 로그인 실패: ${error.message}")
                loginWithKakaoAccount()
            } else if (token != null) {
                Log.d("LoginFragment", "카카오톡으로 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun loginWithKakaoAccount() {
        Log.d("LoginFragment", "loginWithKakaoAccount() 호출됨")
        UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
            if (error != null) {
                Log.e("LoginFragment", "카카오 계정으로 로그인 실패: ${error.message}")
            } else if (token != null) {
                Log.d("LoginFragment", "카카오 계정으로 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun handleLoginSuccess(accessToken: String) {
        Log.d("LoginFragment", "handleLoginSuccess() 호출됨 - AccessToken: $accessToken")
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                val kakaoUserId = user.id.toString()
                Log.d("LoginFragment", "사용자 정보 요청 성공 - 사용자 ID: $kakaoUserId")

                viewModel.setKakaoUserId(kakaoUserId)
                viewModel.setAccessToken(accessToken)

                Log.d("LoginFragment", "서버에 로그인 요청 시작")
                viewModel.loginWithKakaoAccount()
            } else {
                Log.e("LoginFragment", "사용자 정보 요청 실패: ${error?.message}")
                navigateToSignupTermFragment()
            }
        }
    }

    private fun bottomNavigationRemove() {
        Log.d("LoginFragment", "bottomNavigationRemove() 호출됨")
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
        Log.d("LoginFragment", "bottomNavigationShow() 호출됨")
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
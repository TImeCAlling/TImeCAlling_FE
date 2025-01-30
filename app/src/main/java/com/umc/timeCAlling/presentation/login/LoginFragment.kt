package com.umc.timeCAlling.presentation.login

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLoginBinding
import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.sign

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: SignupViewModel by activityViewModels()

    override fun initView() {
        Timber.d("LoginFragment", "initView() 호출됨")
        setClickListener()
        bottomNavigationRemove()
    }

    override fun initObserver() {}

    private fun setClickListener() {
        binding.ivLoginKakaoLogin.setOnClickListener {
            loginWithKakao(requireContext())
        }

        binding.ivNavigateHome.setOnClickListener {
            navigateToHomeFragment()
            bottomNavigationShow()
        }
    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun navigateToSignupTermFragment() {
        Timber.d("LoginFragment", "navigateToSignupTermFragment() 호출됨")
        findNavController().navigate(R.id.action_loginFragment_to_signupTermFragment)
    }

    private fun navigateToHomeFragment() {
        Timber.d("LoginFragment", "navigateToHomeFragment() 호출됨")
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun bottomNavigationShow() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
    }

    fun loginWithKakao(context: Context) {
        Timber.d("LoginFragment", "loginWithKakao(context) 호출됨")
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            Timber.d("LoginFragment", "카카오톡 로그인 시도")
            loginWithKakaoTalk(context)
        } else {
            Timber.d("LoginFragment", "카카오 계정 로그인 시도")
            loginWithKakaoAccount(context)
        }
    }

    private fun loginWithKakaoTalk(context: Context) {
        Timber.d("LoginFragment", "loginWithKakaoTalk() 호출됨")
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Timber.e("LoginFragment", "카카오톡 로그인 실패: ${error.message}", error)
                navigateToSignupTermFragment() // 카카오 로그인 실패 시 회원가입 화면으로 이동
            } else if (token != null) {
                viewModel.setKakaoAccessToken(token.accessToken)
                Timber.d("LoginFragment", "카카오톡 로그인 성공: accessToken = ${token.accessToken}")
                viewModel.handleLoginSuccess(token.accessToken) { isSuccess -> // 콜백 함수 추가
                    if (isSuccess) {
                        navigateToHomeFragment() // 로그인 성공 시 Home 화면으로 이동
                    } else {
                        navigateToSignupTermFragment() // 로그인 실패 시 회원가입 화면으로 이동
                    }
                }
            }
        }
    }

    private fun loginWithKakaoAccount(context: Context) {
        Timber.d("LoginFragment", "loginWithKakaoAccount() 호출됨")
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                Timber.e("LoginFragment", "카카오 계정 로그인 실패: ${error.message}", error)
                navigateToSignupTermFragment() // 카카오 로그인 실패 시 회원가입 화면으로 이동
            } else if (token != null) {
                viewModel.setKakaoAccessToken(token.accessToken)
                Timber.d("LoginFragment", "카카오 계정 로그인 성공: accessToken = ${token.accessToken}")
                viewModel.handleLoginSuccess(token.accessToken) { isSuccess -> // 콜백 함수 추가
                    if (isSuccess) {
                        navigateToHomeFragment() // 로그인 성공 시 Home 화면으로 이동
                    } else {
                        navigateToSignupTermFragment() // 로그인 실패 시 회원가입 화면으로 이동
                    }
                }
            }
        }
    }
}
package com.umc.timeCAlling.presentation.login

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentLoginBinding
import com.umc.timeCAlling.databinding.FragmentSignupBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()


    override fun initView() {
        setClickListener()
        bottomNavigationRemove()
    }

    override fun initObserver() {
        // ViewModel의 로그인 결과를 관찰
        viewModel.loginResult.observe(viewLifecycleOwner, { loginResponse ->
            if (loginResponse != null) {
                navigateToHomeFragment() // 로그인 성공 시 홈 화면으로 이동
            } else {
                navigateToSignupTermFragment() // 로그인 실패 시 약관 동의 화면으로 이동
            }
        })
    }

    private fun setClickListener() {
        binding.ivLoginKakaoLogin.setOnClickListener {
            // 카카오 로그인 버튼 클릭 시
            binding.ivLoginKakaoLogin.setOnClickListener {
                loginWithKakao() // 카카오 로그인 로직 호출
            }
        }

        binding.ivNavigateHome.setOnClickListener {

            navigateToHomeFragment() // 나중에 지우기
            bottomNavigationShow()
        }
    }

    private fun loginWithKakao() {
        val context = requireContext()

        // 카카오톡 로그인 가능 여부 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk()
        } else {
            loginWithKakaoAccount() // 카카오 계정 웹뷰 로그인
        }
    }

    private fun loginWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null) {
                // 카카오톡 로그인 실패 처리
                Timber.e("카카오톡으로 로그인 실패", error)
                loginWithKakaoAccount() // 실패 시 카카오 계정으로 로그인 시도
            } else if (token != null) {
                Timber.i("카카오톡으로 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun loginWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
            if (error != null) {
                // 카카오 계정 로그인 실패 처리
                Timber.e("카카오 계정으로 로그인 실패", error)
            } else if (token != null) {
                Timber.i("카카오 계정으로 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun handleLoginSuccess(accessToken: String) {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                val kakaoUserId = user.id.toString()
                Timber.d("로그인 성공, 사용자 ID: $kakaoUserId")

                // ViewModel에 사용자 ID 및 accessToken 설정
                viewModel.setKakaoUserId(kakaoUserId)
                viewModel.setAccessToken(accessToken)

                // 서버 로그인 처리 호출
                viewModel.loginWithKakaoAccount()
            } else {
                Timber.e("사용자 정보 요청 실패: ${error?.message}")
                navigateToSignupTermFragment()
            }
        }
    }

    private fun bottomNavigationRemove() {
        // BottomNavigationView 숨기기
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        // + 버튼 숨기기
        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.GONE
    }

    private fun navigateToSignupTermFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_signupTermFragment)
    }

    private fun navigateToHomeFragment() {
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
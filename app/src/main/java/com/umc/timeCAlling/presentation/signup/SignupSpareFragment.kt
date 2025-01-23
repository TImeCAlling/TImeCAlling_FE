package com.umc.timeCAlling.presentation.signup

import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupSpareBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupSpareFragment :
    BaseFragment<FragmentSignupSpareBinding>(R.layout.fragment_signup_spare) {

    private val options by lazy {
        listOf(
            binding.tvOnboardingSpareOption1,
            binding.tvOnboardingSpareOption2,
            binding.tvOnboardingSpareOption3
        )
    }

    override fun initView() {
        setDefaultSelectedOption()
        setClickListener()
    }

    override fun initObserver() {
        // 필요한 옵저버를 추가할 수 있습니다.
    }

    private fun setClickListener() {
        // 옵션 선택 버튼 클릭 리스너 설정
        options.forEach { option ->
            option.setOnClickListener {
                updateSelectedOption(option)
            }
        }

        // 뒤로가기 버튼 클릭
        binding.ivOnboardingSpareBack.setOnClickListener {
            findNavController().popBackStack() // Navigation Back Stack에서 이전 화면으로 이동
        }

        // 다음 버튼 클릭
        binding.tvOnboardingSpareNext.setOnClickListener {
            navigateToSignupCompleteFragment() // 다음 화면으로 이동
        }
    }

    private fun setDefaultSelectedOption() {
        updateSelectedOption(binding.tvOnboardingSpareOption2) // '여유'를 기본 선택
    }

    private fun updateSelectedOption(selectedOption: View) {
        options.forEach { option ->
            if (option == selectedOption) {
                applySelectedStyle(option)
            } else {
                applyUnselectedStyle(option)
            }
        }
    }

    private fun applySelectedStyle(option: View) {
        // 선택된 옵션의 스타일 설정
        option.setBackgroundResource(R.drawable.shape_rect_999_mint200_fill_mint_line_1)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Mint)
    }

    private fun applyUnselectedStyle(option: View) {
        // 선택되지 않은 옵션의 스타일 설정
        option.setBackgroundResource(R.drawable.shape_rect_999_gray200_fill)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Gray)
    }

    private fun navigateToSignupCompleteFragment() {
        findNavController().navigate(R.id.action_signupSpareFragment_to_signupCompleteFragment)
    }
}
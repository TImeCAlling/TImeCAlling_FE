package com.umc.timeCAlling.presentation.login

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupSpareBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.presentation.login.adapter.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupSpareFragment :
    BaseFragment<FragmentSignupSpareBinding>(R.layout.fragment_signup_spare) {

    private val signupViewModel: SignupViewModel by activityViewModels()

    private val options by lazy {
        listOf(
            binding.tvSignupSpareOption1,
            binding.tvSignupSpareOption2,
            binding.tvSignupSpareOption3
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
        binding.ivSignupSpareBack.setOnClickListener {
            findNavController().popBackStack() // Navigation Back Stack에서 이전 화면으로 이동
        }

        // 다음 버튼 클릭
        binding.tvSignupSpareNext.setOnClickListener {
            val selectedFreeTimeText = getSelectedFreeTime()
            Log.d("SignupSpareFragment", "선택된 여유시간: $selectedFreeTimeText") // 로그 추가


            val selectedFreeTime = when (selectedFreeTimeText) { // 텍스트를 변환
                "여유" -> "PLENTY"
                "넉넉" -> "RELAXED"
                "딱딱" -> "TIGHT"
                else -> "" // 기본값 또는 오류 처리
            }

            if (selectedFreeTime.isNotEmpty()) {
                signupViewModel.setFreeTime(selectedFreeTime)
                signupViewModel.signup()

                signupViewModel.signupResult.observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        navigateToSignupCompleteFragment()
                    } else {
                        Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "여유시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSelectedFreeTime(): String {
        return options.firstOrNull { it.isSelected }?.text?.toString() ?: ""
    }

    private fun setDefaultSelectedOption() {
        updateSelectedOption(binding.tvSignupSpareOption2) // '여유'를 기본 선택
    }

    private fun updateSelectedOption(selectedOption: View) {
        options.forEach { option ->
            option.isSelected = (option == selectedOption) // 선택 상태 업데이트

            if (option.isSelected) {
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
package com.umc.timeCAlling.presentation.signup

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupNameBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupNameFragment : BaseFragment<FragmentSignupNameBinding>(R.layout.fragment_signup_name) {

    private var isInputValid = false // 입력 상태를 추적

    override fun initView() {
        initEditName() // 입력 필드 초기화
        setClickListener() // 버튼 및 뷰 클릭 리스너 설정

        // 루트 View에 터치 리스너 설정
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = requireActivity().currentFocus
                if (focusedView != null) {
                    val rect = Rect()
                    focusedView.getGlobalVisibleRect(rect)
                    if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        hideKeyboardAndClearFocus()
                    }
                }
            }
            false
        }
    }

    override fun initObserver() {
        // 필요 시 옵저버 추가
    }

    private fun initEditName() {
        // 입력 글자 수 제한
        binding.etOnboardingNameInput.filters = arrayOf(InputFilter.LengthFilter(20))

        // 입력 필드 포커스 및 텍스트 변화 리스너 설정
        binding.etOnboardingNameInput.setOnFocusChangeListener { _, hasFocus ->
            updateInputFieldState(hasFocus, binding.etOnboardingNameInput.text.toString())
        }

        binding.etOnboardingNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() ?: ""
                binding.tvOnboardingNameCount.text = text.length.toString() // 글자 수 업데이트
                isInputValid = text.isNotEmpty() // 입력 유효성 업데이트
                updateInputFieldState(binding.etOnboardingNameInput.hasFocus(), text)

                if (text.isEmpty()) disableNextButton() else enableNextButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setClickListener() {
        binding.tvOnboardingNameNext.setOnClickListener {
            val inputText = binding.etOnboardingNameInput.text.toString().trim()
            if (inputText.isEmpty()) showErrorState() else navigateToSignupTimeFragment()
        }

        binding.clOnboardingNameDelete.setOnClickListener { clearInputField() }

        // 뒤로가기 버튼
        binding.ivOnboardingNameBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // 다음 버튼 활성화
    private fun enableNextButton() {
        binding.tvOnboardingNameNext.apply {
            isEnabled = true
            background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_mint_fill)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    // 다음 버튼 비활성화
    private fun disableNextButton() {
        binding.tvOnboardingNameNext.apply {
            isEnabled = false
            background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_999_gray200_fill)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
        }
    }

    // 에러 상태 표시
    private fun showErrorState() {
        binding.etOnboardingNameInput.apply {
            text = null
            hint = "유효한 문자를 입력해주세요."
            background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_rect_10_white_fill_error_line_1)
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray_500))
        }
        toggleErrorVisibility(true)
        disableNextButton()
    }

    // 에러 상태 숨기기
    private fun hideErrorState() {
        binding.etOnboardingNameInput.hint = "EX) 김지각"
        toggleErrorVisibility(false)
    }

    // 에러, 체크, 삭제 아이콘의 표시 상태를 토글
    private fun toggleErrorVisibility(isError: Boolean) {
        binding.clOnboardingNameError.visibility = if (isError) View.VISIBLE else View.INVISIBLE
        binding.clOnboardingNameCheck.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
        binding.clOnboardingNameDelete.visibility = if (!isError) View.VISIBLE else View.INVISIBLE
    }

    // 입력 필드 상태 업데이트
    private fun updateInputFieldState(hasFocus: Boolean, text: String) {
        hideErrorState() // 에러 상태 숨기기
        when {
            !hasFocus && text.isEmpty() -> setInputFieldStyle(R.drawable.shape_rect_10_gray250_fill, R.color.gray_600)
            hasFocus && text.isEmpty() -> setInputFieldStyle(R.drawable.shape_rect_10_mint100_fill_mint_line_1, R.color.gray_900)
            hasFocus && text.isNotEmpty() -> {
                setInputFieldStyle(R.drawable.shape_rect_10_mint100_fill_mint_line_1, R.color.gray_900)
                binding.clOnboardingNameDelete.visibility = View.VISIBLE
            }
            !hasFocus && text.isNotEmpty() -> {
                setInputFieldStyle(R.drawable.shape_rect_10_white_fill_mint400_line_1, R.color.gray_900)
                binding.clOnboardingNameCheck.visibility = View.VISIBLE
            }
        }
    }

    // 입력 필드 스타일 설정
    private fun setInputFieldStyle(backgroundRes: Int, textColorRes: Int) {
        binding.etOnboardingNameInput.apply {
            background = ContextCompat.getDrawable(requireContext(), backgroundRes)
            setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        }
        binding.clOnboardingNameDelete.visibility = View.INVISIBLE
        binding.clOnboardingNameCheck.visibility = View.INVISIBLE
    }

    private fun hideKeyboardAndClearFocus() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        view?.clearFocus()
    }

    // 입력 필드 초기화
    private fun clearInputField() {
        binding.etOnboardingNameInput.text = null
        binding.etOnboardingNameInput.requestFocus()
    }

    // 다음 화면으로 이동
    private fun navigateToSignupTimeFragment() {
        findNavController().navigate(R.id.action_signupNameFragment_to_signupTimeFragment)
    }
}
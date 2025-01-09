package com.umc.timeCAlling.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingNameBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingNameActivity : BaseActivity<ActivityOnboardingNameBinding>(R.layout.activity_onboarding_name) {

    override fun initView() {
        initEditName()
        setClickListener()
    }

    override fun initObserver() {
        // Observer 설정 (필요 시 추가)
    }

    private fun initEditName() {
        binding.etOnboardingNameInput.filters = arrayOf(InputFilter.LengthFilter(20)) // 최대 글자 수 20자로 제한

        binding.etOnboardingNameInput.setOnFocusChangeListener { _, hasFocus ->
            updateInputFieldState(hasFocus, binding.etOnboardingNameInput.text.toString())
        }

        binding.etOnboardingNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: ""
                binding.tvOnboardingNameCount.text = text.length.toString() // 글자 수 업데이트
                updateInputFieldState(binding.etOnboardingNameInput.hasFocus(), text)

                // 버튼 활성화/비활성화 처리
                if (text.trim().isEmpty()) {
                    disableNextButton()
                } else {
                    enableNextButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setClickListener() {
        binding.tvOnboardingNameNext.setOnClickListener {
            val inputText = binding.etOnboardingNameInput.text.toString()

            if (inputText.trim().isEmpty()) {
                showErrorState() // 공백 입력 처리
            } else {
                navigateToOnboardingTimeActivity()
            }
        }

        binding.clOnboardingNameDelete.setOnClickListener {
            clearInputField()
        }
    }

    private fun enableNextButton() {
        binding.tvOnboardingNameNext.isEnabled = true
        binding.tvOnboardingNameNext.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_999_mint_fill)
        binding.tvOnboardingNameNext.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun disableNextButton() {
        binding.tvOnboardingNameNext.isEnabled = false
        binding.tvOnboardingNameNext.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_999_gray200_fill)
        binding.tvOnboardingNameNext.setTextColor(ContextCompat.getColor(this, R.color.gray_500))
    }

    private fun showErrorState() {
        binding.etOnboardingNameInput.text = null // 입력 텍스트 삭제
        binding.etOnboardingNameInput.hint = "유효한 문자를 입력해주세요." // 힌트 변경
        binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_10_white_fill_error_line_1) // 배경 변경
        binding.etOnboardingNameInput.setHintTextColor(ContextCompat.getColor(this, R.color.gray_500)) // 힌트 색상 변경

        binding.clOnboardingNameDelete.visibility = View.INVISIBLE // delete 버튼 숨기기
        binding.clOnboardingNameCheck.visibility = View.INVISIBLE // 체크 표시 숨기기
        binding.clOnboardingNameError.visibility = View.VISIBLE // 에러 표시 보이기

        disableNextButton() // 버튼 비활성화
    }

    private fun hideErrorState() {
        binding.clOnboardingNameError.visibility = View.INVISIBLE // 에러 표시
        binding.etOnboardingNameInput.hint = "EX) 김지각" // 힌트 변경
    }

    private fun updateInputFieldState(hasFocus: Boolean, text: String) {
        hideErrorState() // 에러 표시 숨기기
        when {
            !hasFocus && text.isEmpty() -> handleNoFocusNoText()
            hasFocus && text.isEmpty() -> handleFocusNoText()
            hasFocus && text.isNotEmpty() -> handleFocusWithText()
            !hasFocus && text.isNotEmpty() -> handleNoFocusWithText()
        }
    }

    private fun handleNoFocusNoText() {
        binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_10_gray250_fill) // 배경 변경
        binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this, R.color.gray_600))
        binding.clOnboardingNameCheck.visibility = View.INVISIBLE
        binding.clOnboardingNameDelete.visibility = View.INVISIBLE

        disableNextButton()
    }

    private fun handleFocusNoText() {
        binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_10_mint100_fill_mint_line_1) // 배경 변경
        binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this, R.color.gray_900))
        binding.clOnboardingNameDelete.visibility = View.INVISIBLE
        binding.clOnboardingNameCheck.visibility = View.INVISIBLE
    }

    private fun handleFocusWithText() {
        binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_10_mint100_fill_mint_line_1) // 배경 변경
        binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this, R.color.gray_900))
        binding.clOnboardingNameDelete.visibility = View.VISIBLE
        binding.clOnboardingNameCheck.visibility = View.INVISIBLE
    }

    private fun handleNoFocusWithText() {
        binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this, R.drawable.shape_rect_10_white_fill_mint400_line_1) // 배경 변경
        binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this, R.color.gray_900))
        binding.clOnboardingNameDelete.visibility = View.INVISIBLE
        binding.clOnboardingNameCheck.visibility = View.VISIBLE

        enableNextButton()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val rect = Rect()
            currentFocus?.getGlobalVisibleRect(rect)

            if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                if (binding.clOnboardingNameDelete.isShown) {
                    val deleteRect = Rect()
                    binding.clOnboardingNameDelete.getGlobalVisibleRect(deleteRect)
                    if (deleteRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        return super.dispatchTouchEvent(ev)
                    }
                }

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                currentFocus?.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun clearInputField() {
        binding.etOnboardingNameInput.text = null
        binding.etOnboardingNameInput.requestFocus()
    }

    private fun navigateToOnboardingTimeActivity() {
        startActivity(Intent(this, OnboardingTimeActivity::class.java))
    }
}
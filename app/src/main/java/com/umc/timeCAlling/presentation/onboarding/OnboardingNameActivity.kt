package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingNameBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingNameActivity : BaseActivity<ActivityOnboardingNameBinding>(R.layout.activity_onboarding_name) {

    override fun initView() {

        initProfileName()
        setClickListener()
    }

    override fun initObserver() {

    }

    private fun initProfileName() {
        binding.etOnboardingNameInput.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10)) // 최대 글자 수 10글자로 제한

        binding.etOnboardingNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0 // 입력된 글자 수
                binding.tvOnboardingNameCount.text = textLength.toString() // 글자 수 표시

                if (textLength > 0) { // 글자가 입력된 경우
                    binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this@OnboardingNameActivity, R.drawable.shape_rect_10_white_fill_mint_line_2) // 배경 변경
                    binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this@OnboardingNameActivity, R.color.gray_900)) // 글자 색깔 변경
                    binding.clOnboardingNameCheck.visibility = View.VISIBLE // 체크 표시 보이기
                } else { // 글자가 없는 경우
                    binding.etOnboardingNameInput.background = ContextCompat.getDrawable(this@OnboardingNameActivity, R.drawable.shape_rect_10_gray250_fill) // 배경 변경
                    binding.etOnboardingNameInput.setTextColor(ContextCompat.getColor(this@OnboardingNameActivity, R.color.gray_600)) // 글자 색깔 변경
                    binding.clOnboardingNameCheck.visibility = View.INVISIBLE // 체크 표시 숨기기
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후
            }
        })
    }

    private fun setClickListener() {
        binding.tvOnboardingNameNext.setOnClickListener {

            navigateToOnboardingTimeActivity()
        }
    }

    private fun navigateToOnboardingTimeActivity() {
        val intent = Intent(this, OnboardingTimeActivity::class.java)
        startActivity(intent)
    }
}
package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingSpareBinding
import com.umc.timeCAlling.presentation.MainActivity
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSpareActivity :
    BaseActivity<ActivityOnboardingSpareBinding>(R.layout.activity_onboarding_spare) {

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

    }

    private fun setClickListener() {
        options.forEach { option ->
            option.setOnClickListener {
                updateSelectedOption(option)
            }
        }

        binding.tvOnboardingSpareNext.setOnClickListener {
            navigateToMainActivity()
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
        option.setBackgroundResource(R.drawable.shape_rect_999_mint200_fill_mint_line_1)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Mint)
    }

    private fun applyUnselectedStyle(option: View) {
        option.setBackgroundResource(R.drawable.shape_rect_999_gray200_fill)
        (option as? TextView)?.setTextAppearance(R.style.TextAppearance_TimeCAlling_Button_Gray)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
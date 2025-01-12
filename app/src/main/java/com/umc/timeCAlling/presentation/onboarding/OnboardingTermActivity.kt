package com.umc.timeCAlling.presentation.onboarding

import android.content.Intent
import android.view.View
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ActivityOnboardingTermBinding
import com.umc.timeCAlling.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingTermActivity : BaseActivity<ActivityOnboardingTermBinding>(R.layout.activity_onboarding_term) {

    override fun initView() {
        setClickListener()
        setupTermsContent()
    }

    override fun initObserver() {

    }

    private fun setClickListener() {
        val termButtons = listOf(
            binding.clOnboardingTermBtn1 to binding.ivOnboardingTermOval1,
            binding.clOnboardingTermBtn2 to binding.ivOnboardingTermOval2,
            binding.clOnboardingTermBtn3 to binding.ivOnboardingTermOval3,
            binding.clOnboardingTermBtn4 to binding.ivOnboardingTermOval4,
            binding.clOnboardingTermBtn5 to binding.ivOnboardingTermOval5
        )

        binding.clOnboardingTermBtnAll.setOnClickListener {
            val isSelected = toggleButton(binding.ivOnboardingTermOvalAll)
            termButtons.forEach { (_, oval) ->
                setButtonBackground(oval, isSelected)
            }
            updateNextButtonState()
        }

        termButtons.forEach { (container, oval) ->
            container.setOnClickListener {
                val isSelected = toggleButton(oval)
                if (!isSelected) {
                    setButtonBackground(binding.ivOnboardingTermOvalAll, false)
                } else if (termButtons.all { (_, oval) -> oval.tag == true }) {
                    setButtonBackground(binding.ivOnboardingTermOvalAll, true)
                }
                updateNextButtonState()
            }
        }

        // 약관 화살표 클릭 이벤트
        setupArrowClickListeners()

        // 뒤로가기 버튼
        binding.ivOnboardingTermBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 다음 버튼
        binding.tvOnboardingTermNext.setOnClickListener {
            if (isNextButtonEnabled()) {
                navigateToOnboardingPhotoActivity()
            }
        }
    }

    private fun toggleButton(button: View): Boolean {
        val isSelected = button.tag as? Boolean ?: false
        setButtonBackground(button, !isSelected)
        return !isSelected
    }

    private fun setButtonBackground(button: View, isSelected: Boolean) {
        button.setBackgroundResource(
            if (isSelected) R.drawable.shape_rect_999_mint_fill else R.drawable.shape_rect_999_gray300_fill
        )
        button.tag = isSelected
    }

    private fun updateNextButtonState() {
        val allRequiredSelected = listOf(
            binding.ivOnboardingTermOval1,
            binding.ivOnboardingTermOval2,
            binding.ivOnboardingTermOval3,
            binding.ivOnboardingTermOval4
        ).all { it.tag == true }

        binding.tvOnboardingTermNext.apply {
            isClickable = allRequiredSelected
            isEnabled = allRequiredSelected
            setBackgroundResource(
                if (allRequiredSelected) R.drawable.shape_rect_999_mint_fill else R.drawable.shape_rect_999_gray300_fill
            )
            setTextAppearance(
                if (allRequiredSelected) R.style.TextAppearance_TimeCAlling_Button
                else R.style.TextAppearance_TimeCAlling_Button_Gray
            )
        }
    }

    private fun setupArrowClickListeners() {
        val arrowConfigs = listOf(
            Triple(binding.ivOnboardingTermArrowUp2, binding.ivOnboardingTermArrowDown2, binding.svOnboardingTermContent2),
            Triple(binding.ivOnboardingTermArrowUp3, binding.ivOnboardingTermArrowDown3, binding.tvOnboardingTermContent3),
            Triple(binding.ivOnboardingTermArrowUp4, binding.ivOnboardingTermArrowDown4, binding.tvOnboardingTermContent4)
        )

        arrowConfigs.forEach { (arrowUp, arrowDown, contentView) ->
            arrowUp.setOnClickListener {
                arrowUp.visibility = View.INVISIBLE
                arrowDown.visibility = View.VISIBLE
                contentView.visibility = View.GONE
            }

            arrowDown.setOnClickListener {
                arrowDown.visibility = View.INVISIBLE
                arrowUp.visibility = View.VISIBLE
                contentView.visibility = View.VISIBLE
            }
        }
    }

    // 약관 내용
    private fun loadTermsContent(resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }

    private fun setupTermsContent() {
        binding.tvOnboardingTermContent2.text = loadTermsContent(R.raw.term_1)
        // 추가하기
    }

    private fun isNextButtonEnabled(): Boolean {
        return binding.tvOnboardingTermNext.isClickable
    }

    private fun navigateToOnboardingPhotoActivity() {
            val intent = Intent(this, OnboardingPhotoActivity::class.java)
            startActivity(intent)
    }
}

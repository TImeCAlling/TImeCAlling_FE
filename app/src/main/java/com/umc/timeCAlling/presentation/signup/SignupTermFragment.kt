package com.umc.timeCAlling.presentation.signup

import android.view.View
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentSignupTermBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupTermFragment : BaseFragment<FragmentSignupTermBinding>(R.layout.fragment_signup_term) {

    override fun initView() {
        setClickListener()
        setupTermsContent()
    }

    override fun initObserver() {
        // 필요한 옵저버 설정
    }

    private fun setClickListener() {
        val termButtons = listOf(
            binding.clOnboardingTermBtn1 to binding.ivOnboardingTermOval1,
            binding.clOnboardingTermBtn2 to binding.ivOnboardingTermOval2,
            binding.clOnboardingTermBtn3 to binding.ivOnboardingTermOval3,
            binding.clOnboardingTermBtn4 to binding.ivOnboardingTermOval4,
            binding.clOnboardingTermBtn5 to binding.ivOnboardingTermOval5
        )

        // '모두 동의' 버튼
        binding.clOnboardingTermBtnAll.setOnClickListener {
            val isSelected = toggleButton(binding.ivOnboardingTermOvalAll)
            termButtons.forEach { (_, oval) ->
                setButtonBackground(oval, isSelected)
            }
            updateNextButtonState()
        }

        // 각 약관 버튼
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
            findNavController().popBackStack() // Navigation Back Stack을 통해 뒤로 이동
        }

        // 다음 버튼
        binding.tvOnboardingTermNext.setOnClickListener {
            if (isNextButtonEnabled()) {
                navigateToSignupPhotoFragment() // 다음 화면으로 이동
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
            setTextColor(
                requireContext().getColor(
                    if (allRequiredSelected) R.color.white else R.color.gray_500
                )
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

    private fun loadTermsContent(resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }

    private fun setupTermsContent() {
        binding.tvOnboardingTermContent2.text = loadTermsContent(R.raw.term_1)
        // 추가 약관 내용을 설정할 수 있습니다.
    }

    private fun isNextButtonEnabled(): Boolean {
        return binding.tvOnboardingTermNext.isClickable
    }

    private fun navigateToSignupPhotoFragment() {
        findNavController().navigate(R.id.action_signupTermFragment_to_signupPhotoFragment)
    }
}
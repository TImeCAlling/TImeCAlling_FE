package com.umc.timeCAlling.presentation.login

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
            binding.clSignupTermBtn1 to binding.ivSignupTermOval1,
            binding.clSignupTermBtn2 to binding.ivSignupTermOval2,
            binding.clSignupTermBtn3 to binding.ivSignupTermOval3,
            binding.clSignupTermBtn4 to binding.ivSignupTermOval4,
            binding.clSignupTermBtn5 to binding.ivSignupTermOval5
        )

        // '모두 동의' 버튼
        binding.clSignupTermBtnAll.setOnClickListener {
            val isSelected = toggleButton(binding.ivSignupTermOvalAll)
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
                    setButtonBackground(binding.ivSignupTermOvalAll, false)
                } else if (termButtons.all { (_, oval) -> oval.tag == true }) {
                    setButtonBackground(binding.ivSignupTermOvalAll, true)
                }
                updateNextButtonState()
            }
        }

        // 약관 화살표 클릭 이벤트
        setupArrowClickListeners()

        // 뒤로가기 버튼
        binding.ivSignupTermBack.setOnClickListener {
            findNavController().popBackStack() // Navigation Back Stack을 통해 뒤로 이동
        }

        // 다음 버튼
        binding.tvSignupTermNext.setOnClickListener {
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
            binding.ivSignupTermOval1,
            binding.ivSignupTermOval2,
            binding.ivSignupTermOval3,
            binding.ivSignupTermOval4
        ).all { it.tag == true }

        binding.tvSignupTermNext.apply {
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
            Triple(binding.ivSignupTermArrowUp2, binding.ivSignupTermArrowDown2, binding.svSignupTermContent2),
            Triple(binding.ivSignupTermArrowUp3, binding.ivSignupTermArrowDown3, binding.tvSignupTermContent3),
            Triple(binding.ivSignupTermArrowUp4, binding.ivSignupTermArrowDown4, binding.tvSignupTermContent4)
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
        binding.tvSignupTermContent2.text = loadTermsContent(R.raw.term_1)
        // 추가 약관 내용을 설정할 수 있습니다.
    }

    private fun isNextButtonEnabled(): Boolean {
        return binding.tvSignupTermNext.isClickable
    }

    private fun navigateToSignupPhotoFragment() {
        findNavController().navigate(R.id.action_signupTermFragment_to_signupPhotoFragment)
    }
}
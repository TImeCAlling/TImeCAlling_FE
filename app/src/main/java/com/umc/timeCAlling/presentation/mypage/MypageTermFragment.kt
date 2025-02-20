package com.umc.timeCAlling.presentation.mypage

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageTermBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@AndroidEntryPoint
class MypageTermFragment: BaseFragment<FragmentMypageTermBinding>(R.layout.fragment_mypage_term) {

    override fun initObserver() {
    }

    override fun initView() {
        setClickListener()
        hideViews(
            R.id.main_bnv,
            R.id.iv_main_add_schedule_btn,
            R.id.iv_main_bnv_shadow,
            R.id.iv_main_bnv_white_oval
        )
    }

    private fun hideViews(vararg viewIds: Int) {
        viewIds.forEach { id ->
            requireActivity().findViewById<View>(id)?.visibility = View.GONE
        }
    }

    private fun setClickListener() {
        binding.ivMypageBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMypageTermBack.setOnClickListener { showTermList() }

        // 각 이용약관 항목 클릭 시 이용약관 본문 보기
        val termItems = listOf(
            binding.tvMypageTerm1 to binding.ivMypageTerm1Arrow,
            binding.tvMypageTerm2 to binding.ivMypageTerm2Arrow,
            binding.tvMypageTerm3 to binding.ivMypageTerm3Arrow,
            binding.tvMypageTerm4 to binding.ivMypageTerm4Arrow,
            binding.tvMypageTerm5 to binding.ivMypageTerm5Arrow,
            binding.tvMypageTerm6 to binding.ivMypageTerm6Arrow
        )

        val fileResources = listOf(
            R.raw.term_1, R.raw.term_2, R.raw.term_3,
            R.raw.term_4, R.raw.term_5, R.raw.term_6
        )

        termItems.forEachIndexed { index, (textView, imageView) ->
            val resourceId = fileResources[index]

            textView.setOnClickListener { showTermContent(textView.text.toString(), resourceId) }
            imageView.setOnClickListener { showTermContent(textView.text.toString(), resourceId) }
        }
    }

    private fun showTermContent(title: String, resourceId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val content = loadTermsContent(resourceId)
            launch(Dispatchers.Main) {
                binding.tvMypageTitle.text = title
                binding.tvMypageTermContent.text = content
                binding.ivMypageBack.isVisible = false
                binding.ivMypageTermBack.isVisible = true
                binding.svMypageTermContent.isVisible = true
                toggleTermListVisibility(false)
            }
        }
    }

    private fun showTermList() {
        binding.ivMypageBack.isVisible = true
        binding.ivMypageTermBack.isVisible = false
        binding.svMypageTermContent.isVisible = false
        binding.tvMypageTitle.text = "이용약관"
        toggleTermListVisibility(true)
    }

    private fun toggleTermListVisibility(isVisible: Boolean) {
        binding.tvMypageTerm1.isVisible = isVisible
        binding.ivMypageTerm1Arrow.isVisible = isVisible
        binding.tvMypageTerm2.isVisible = isVisible
        binding.ivMypageTerm2Arrow.isVisible = isVisible
        binding.tvMypageTerm3.isVisible = isVisible
        binding.ivMypageTerm3Arrow.isVisible = isVisible
        binding.tvMypageTerm4.isVisible = isVisible
        binding.ivMypageTerm4Arrow.isVisible = isVisible
        binding.tvMypageTerm5.isVisible = isVisible
        binding.ivMypageTerm5Arrow.isVisible = isVisible
        binding.tvMypageTerm6.isVisible = isVisible
        binding.ivMypageTerm6Arrow.isVisible = isVisible
    }

    private fun loadTermsContent(resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }
}
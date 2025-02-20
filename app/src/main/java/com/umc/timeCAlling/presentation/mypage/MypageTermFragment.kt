package com.umc.timeCAlling.presentation.mypage

import android.view.View
import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentMypageTermBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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

    private fun setClickListener(){
        binding.ivMyprofileBack.setOnClickListener { findNavController().popBackStack() }
    }
}
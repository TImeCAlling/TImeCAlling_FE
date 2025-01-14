package com.umc.timeCAlling.presentation.checkList

import androidx.navigation.fragment.findNavController
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCheckListResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListResultFragment: BaseFragment<FragmentCheckListResultBinding>(R.layout.fragment_check_list_result) {

    override fun initView() {
        binding.apply {
            btnChecklistResult.setOnClickListener {
                findNavController().navigate(R.id.action_checkListResultFragment_to_homeFragment)
            }
        }
    }

    override fun initObserver() {

    }
}
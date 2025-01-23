package com.umc.timeCAlling.presentation.checkList

import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.FragmentCheckListResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListResultFragment: BaseFragment<FragmentCheckListResultBinding>(R.layout.fragment_check_list_result) {
    private lateinit var args : CheckListResultFragmentArgs

    override fun initView() {
        args = CheckListResultFragmentArgs.fromBundle(requireArguments())
        val idx = args.scheduleIndex
        binding.apply {
            btnChecklistResult.setOnClickListener {
                val action = CheckListResultFragmentDirections.actionCheckListResultFragmentToHomeFragment(idx)
                findNavController().navigate(action)
                bottomNavigationShow()
            }
        }
    }

    override fun initObserver() {

    }

    private fun bottomNavigationShow() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.VISIBLE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.VISIBLE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.VISIBLE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        ovalImageView?.visibility = View.VISIBLE
    }
}
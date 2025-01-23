package com.umc.timeCAlling.presentation.calendar.wakeup

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.DialogWakeupBinding
import com.umc.timeCAlling.databinding.DialogWakeupShareBinding
import com.umc.timeCAlling.databinding.FragmentWakeupBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakeupFragment: BaseFragment<FragmentWakeupBinding>(R.layout.fragment_wakeup) {

    private lateinit var wakeupPeopleRVA: WakeupPeopleRVA

    override fun initObserver() {

    }

    override fun initView() {

        bottomNavigationShow()

        binding.ivWakeupShare.setOnSingleClickListener {
            showWakeupShareDialog()
        }

        binding.ivWakeupBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }

        wakeupPeopleRVA = WakeupPeopleRVA()
        binding.rvWakeupPeople.adapter = wakeupPeopleRVA
        binding.rvWakeupPeople.layoutManager = LinearLayoutManager(requireContext())
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

    private fun showWakeupShareDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_wakeup_share, null)
        val dialogBinding = DialogWakeupShareBinding.bind(dialogView)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val layoutParams = dialog.window?.attributes
        layoutParams?.dimAmount = 0.8f
        dialog.window?.attributes = layoutParams

        dialogBinding.tvDialogSuccess.setOnSingleClickListener {
            dialog.dismiss()
        }
    }
}

package com.umc.timeCAlling.presentation.calendar.wakeup

import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.DialogWakeupBinding
import com.umc.timeCAlling.databinding.FragmentAddScheduleSecondBinding
import com.umc.timeCAlling.databinding.FragmentWakeupBinding
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakeupFragment: BaseFragment<FragmentWakeupBinding>(R.layout.fragment_wakeup) {

    override fun initObserver() {

    }

    override fun initView() {

        binding.ivWakeupShare.setOnSingleClickListener {
            showProfileConfirmDialog()
        }

    }

    private fun showProfileConfirmDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_wakeup, null)
        val dialogBinding = DialogWakeupBinding.bind(dialogView)

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

package com.umc.timeCAlling.presentation.calendar.wakeup

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.text
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.DialogWakeupBinding
import com.umc.timeCAlling.databinding.DialogWakeupShareBinding
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel
import com.umc.timeCAlling.util.extension.setOnSingleClickListener

class WakeupPeopleRVA : RecyclerView.Adapter<WakeupPeopleRVA.WakeupPeopleViewHolder>() {

    private var userList: List<ScheduleUsersResponseModel> = emptyList()

    class WakeupPeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.iv_wakeup_people_profile)
        val nameTextView: TextView = itemView.findViewById(R.id.tv_wakeup_people_name)
        val wakeup: ImageView = itemView.findViewById(R.id.iv_wakeup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WakeupPeopleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wakeup_people, parent, false)
        return WakeupPeopleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WakeupPeopleViewHolder, position: Int) {
        val user = userList[position]
        // 이미지를 Glide를 사용하여 불러옵니다.
        Glide.with(holder.itemView.context)
            .load(user.profile)
            .into(holder.profileImageView)
        holder.nameTextView.text = user.nickname

        holder.wakeup.setOnClickListener {
            holder.wakeup.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.mint_main))
            Log.d("WakeupPeopleRVA", "Next button clicked for ${user.nickname}")
            showWakeupShareDialog(holder, user.nickname)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateList(newList: List<ScheduleUsersResponseModel>) {
        userList = newList
        notifyDataSetChanged()
    }

    private fun showWakeupShareDialog(holder: WakeupPeopleViewHolder, userName: String) {
        val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.dialog_wakeup, null)
        val dialogBinding = DialogWakeupBinding.bind(dialogView)

        dialogBinding.tvDialogMessage.text = "${userName}님께 깨우기 알람을 보냈어요!"

        val dialog = MaterialAlertDialogBuilder(holder.itemView.context)
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
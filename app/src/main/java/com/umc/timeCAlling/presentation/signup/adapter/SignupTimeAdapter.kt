package com.umc.timeCAlling.presentation.signup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ItemOnboardingTimeBinding

class SignupTimeAdapter(
    private val timeOptions: List<String>
) : RecyclerView.Adapter<SignupTimeAdapter.TimeViewHolder>() {

    private var selectedPosition: Int? = null

    inner class TimeViewHolder(private val binding: ItemOnboardingTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String, isSelected: Boolean) {
            binding.tvOnboardingTimeItem.text = time
            binding.tvOnboardingTimeItem.setTextColor(
                if (isSelected) binding.root.context.getColor(R.color.mint_main)
                else binding.root.context.getColor(R.color.gray_400)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemOnboardingTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(timeOptions[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = timeOptions.size

    fun updateItemColor(position: Int, isSelected: Boolean) {
        if (isSelected) {
            selectedPosition = position
        } else if (selectedPosition == position) {
            selectedPosition = null
        }
        notifyItemChanged(position)
    }
}
package com.umc.timeCAlling.presentation.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.databinding.ItemOnboardingTimeBinding

class OnboardingTimeAdapter(
    private val timeOptions: List<String>
) : RecyclerView.Adapter<OnboardingTimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(private val binding: ItemOnboardingTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {
            binding.tvOnboardingTimeItem.text = time
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
        holder.bind(timeOptions[position])
    }

    override fun getItemCount(): Int = timeOptions.size
}
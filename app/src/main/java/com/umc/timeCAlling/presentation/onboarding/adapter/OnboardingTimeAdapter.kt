package com.umc.timeCAlling.presentation.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.databinding.ItemOnboardingTimeBinding

class OnboardingTimeAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, OnboardingTimeAdapter.TimeViewHolder>(DiffCallback) {

    inner class TimeViewHolder(private val binding: ItemOnboardingTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {
            binding.tvOnboardingTimeItem.text = time
            binding.root.setOnClickListener {
                onItemClick(time) // 클릭 시 호출
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemOnboardingTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
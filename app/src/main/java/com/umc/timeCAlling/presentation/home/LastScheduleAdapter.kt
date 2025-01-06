package com.umc.timeCAlling.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.databinding.ItemLastScheduleBinding

class LastScheduleAdapter(private val lastSchedules: List<LastSchedule>) : RecyclerView.Adapter<LastScheduleAdapter.LastScheduleViewHolder>() {
    inner class LastScheduleViewHolder(private val binding: ItemLastScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lastSchedule: LastSchedule) {
            binding.tvLastScheduleTitle.text = lastSchedule.title
            binding.tvLastScheduleDescription.text = lastSchedule.description
            binding.tvLastScheduleTimeType.text = if (lastSchedule.isMorning) "오전" else "오후"
            binding.tvLastScheduleTime.text = lastSchedule.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastScheduleViewHolder {
        val view = ItemLastScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastScheduleViewHolder(view)
    }

    override fun getItemCount(): Int = lastSchedules.size

    override fun onBindViewHolder(holder: LastScheduleViewHolder, position: Int) {
        holder.bind(lastSchedules[position])
    }
}

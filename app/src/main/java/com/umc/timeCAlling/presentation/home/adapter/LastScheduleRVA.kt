package com.umc.timeCAlling.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.databinding.ItemLastScheduleBinding
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel

class LastScheduleRVA(
) : RecyclerView.Adapter<LastScheduleRVA.LastScheduleViewHolder>() {
    lateinit var onItemClick: ((DetailScheduleResponseModel) -> Unit)
    private var lastSchedules: List<DetailScheduleResponseModel> = emptyList()

    fun setLastScheduleList(scheduleList: List<DetailScheduleResponseModel>) {
        this.lastSchedules = scheduleList
        notifyDataSetChanged()
    }

    inner class LastScheduleViewHolder(private val binding: ItemLastScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvLastScheduleTitle
        val description = binding.tvLastScheduleDescription
        val timeType = binding.tvLastScheduleTimeType
        val time = binding.tvLastScheduleTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastScheduleViewHolder {
        val view = ItemLastScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastScheduleViewHolder(view)
    }

    override fun getItemCount(): Int = lastSchedules.size

    override fun onBindViewHolder(holder: LastScheduleViewHolder, position: Int) {

        holder.title.text = lastSchedules[position].name
        holder.description.text = lastSchedules[position].body
        val meetTime = lastSchedules[position].meetTime
        val parsedTime = parseTimeString(meetTime)
        if(parsedTime != null) {
            val (hours, minutes) = parsedTime
            val formattedHours = if (hours == 0) 12 else if (hours > 12) hours - 12 else hours
            val formattedMinutes = String.format("%02d", minutes)
            val timeType = if (hours < 12) "오전" else "오후"

            holder.timeType.text = timeType
            holder.time.text = "${String.format("%02d", formattedHours)}:${formattedMinutes}"
        }

        holder.itemView.setOnClickListener {
            onItemClick.invoke(lastSchedules[position])
        }
    }

    private fun parseTimeString(timeString: String): Pair<Int, Int>? {
        // 입력 문자열이 유효한 형식인지 확인
        if (!timeString.matches(Regex("\\d{2}:\\d{2}"))) {
            return null
        }
        val parts = timeString.split(":")
        if (parts.size != 2) {
            return null
        }

        val hours = parts[0].toIntOrNull()
        val minutes = parts[1].toIntOrNull()

        // 변환 실패 시 null 반환
        if (hours == null || minutes == null) {
            return null
        }

        return Pair(hours, minutes)
    }
}

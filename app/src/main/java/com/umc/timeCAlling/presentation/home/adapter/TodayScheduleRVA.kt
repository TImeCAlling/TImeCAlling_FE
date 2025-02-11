package com.umc.timeCAlling.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ItemTodayScheduleBinding
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.presentation.home.TodaySchedule
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class TodayScheduleRVA(
) : RecyclerView.Adapter<TodayScheduleRVA.ViewHolder>() {

    private var items = ArrayList<TodayScheduleResponseModel>()
    fun setScheduleList(scheduleList: ArrayList<TodayScheduleResponseModel>) {
        this.items = scheduleList
        notifyDataSetChanged()
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    inner class ViewHolder(val binding: ItemTodayScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTodayScheduleTitle
        val description = binding.tvTodayScheduleDescription
        val time = binding.tvTodayScheduleTime
        val timeLeft = binding.tvTodayScheduleTimeLeft
        val timeType = binding.tvTodayScheduleTimeType
        val more = binding.imgTodayScheduleMore

        val view = binding.viewTodaySchedule
        val bar = binding.viewScheduleBar
        val container = binding.layoutTodayScheduleContainer
        val timeLeftContainer = binding.layoutTodayScheduleTimeLeft
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodayScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].name
        holder.description.text = items[position].body

        val meetTime = items[position].meetTime
        val parsedTime = parseTimeString(meetTime)
        if(parsedTime != null) {
            val (hours, minutes, seconds) = parsedTime
            holder.timeType.text = if(hours < 12) "오전" else "오후"
            holder.time.text = "${hours}:${minutes}"
            holder.timeLeft.text = ""
        }
        val leftMin = calculateMinutesUntilSpecificTime(meetTime)
        holder.timeLeft.text = leftMin.toString() + "\nmin"

        //제일 가까운 일정 ( 첫번째 인덱스 )는 민트색
        if(position == 0) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_16_mint_fill_mint_line_1)
            holder.timeLeftContainer.setBackgroundResource(R.drawable.shape_rect_999_white_fill)
            holder.more.setImageResource(R.drawable.ic_home_more)
            holder.time.setTextColor(holder.timeType.context.getColor(R.color.mint_main))
            holder.description.setTextColor(holder.timeType.context.getColor(R.color.white))
        }

        if(position == items.size - 1) holder.bar.visibility = View.GONE

        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.more.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${items[position].name} Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size

    private fun parseTimeString(timeString: String): Triple<Int, Int, Int>? {
        // 입력 문자열이 유효한 형식인지 확인
        if (!timeString.matches(Regex("\\d{2}:\\d{2}:\\d{2}"))) {
            return null
        }
        val parts = timeString.split(":")
        if (parts.size != 3) {
            return null
        }

        val hours = parts[0].toIntOrNull()
        val minutes = parts[1].toIntOrNull()
        val seconds = parts[2].toIntOrNull()

        // 변환 실패 시 null 반환
        if (hours == null || minutes == null || seconds == null) {
            return null
        }

        return Triple(hours, minutes, seconds)
    }

    private fun calculateMinutesUntilSpecificTime(specificTime: String): Long {
        // 현재 시간 가져오기
        val now = LocalDateTime.now()

        // 특정 시간 파싱 (hh:mm:ss 형식)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val parsedTime = LocalTime.parse(specificTime, timeFormatter)

        // 특정 시간의 LocalDateTime 생성
        var specificDateTime = LocalDateTime.of(LocalDate.now(), parsedTime)

        // 특정 시간이 현재 시간보다 이전일 경우, 다음 날로 계산
        if (specificDateTime.isBefore(now)) {
            specificDateTime = specificDateTime.plusDays(1)
        }

        // 시간 차이 계산
        val duration = Duration.between(now, specificDateTime)

        // 분 단위로 변환
        return duration.toMinutes()
    }
}
package com.umc.timeCAlling.presentation.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ItemTodayScheduleDetailBinding
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class DetailScheduleRVA() : RecyclerView.Adapter<DetailScheduleRVA.DetailScheduleViewHolder>() {
    lateinit var onItemClick: ((ScheduleByDateResponseModel) -> Unit)
    private var detailSchedules : List<ScheduleByDateResponseModel> = emptyList()

    fun setScheduleList(scheduleList: List<ScheduleByDateResponseModel>) {
        this.detailSchedules = ArrayList(scheduleList)
        Log.d("DetailScheduleRVA", "setScheduleList 호출됨")
        notifyDataSetChanged()
    }

    inner class DetailScheduleViewHolder(private val binding: ItemTodayScheduleDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvDetailScheduleTitle
        val repeatInfo = binding.tvDetailScheduleRepeatInfo
        val category = binding.tvDetailScheduleCategory
        val time = binding.tvDetailScheduleTime
        val timeType = binding.tvDetailScheduleTimeType

        val memberFirst = binding.cvDetailScheduleMemberFirst
        val memberSecond = binding.cvDetailScheduleMemberSecond
        val extraMembers = binding.cvDetailScheduleExtraMembers
        val extraMembersCount = binding.tvDetailScheduleExtraMembers

        val view = binding.viewDetailSchedule
        val bar = binding.viewDetailScheduleBar
        val container = binding.layoutDetailScheduleContainer
        val checkBtn = binding.imgDetailScheduleCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailScheduleViewHolder {
        val view = ItemTodayScheduleDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailScheduleViewHolder, position: Int) {
        var isChecked = false
        val meetTime = detailSchedules[position].meetTime
        val parsedTime = parseTimeString(meetTime)
        if(parsedTime != null) {
            val (hours, minutes, seconds) = parsedTime
            val formattedHours = if (hours == 0) 12 else if (hours > 12) hours - 12 else hours
            val formattedMinutes = String.format("%02d", minutes)

            holder.timeType.text = if (hours < 12) "오전" else "오후"
            holder.time.text = "${String.format("%02d", formattedHours)}:${formattedMinutes}"
        }

        holder.title.text = detailSchedules[position].name
        if(detailSchedules[position].repeatDays != null) {
            holder.repeatInfo.text = repeatDaysToKo(detailSchedules[position].repeatDays!!)
        } else {
            holder.repeatInfo.text = ""
        }
        holder.category.text = detailSchedules[position].categories[0].categoryName //카테고리 색 반영 나중에

        if(position == 0) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_28_mint200_fill_mint_line_1)
            holder.time.setTextColor(holder.time.context.getColor(R.color.mint_600))
        }

        holder.bar.visibility = if(position == detailSchedules.size -1) View.GONE else View.VISIBLE

        holder.checkBtn.setOnClickListener {
            if(!isChecked) {
                holder.checkBtn.setImageResource(R.drawable.ic_schedule_detail_check_mint)
                isChecked = true
            } else {
                holder.checkBtn.setImageResource(R.drawable.ic_schedule_detail_check)
                isChecked = false
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick.invoke(detailSchedules[position])
        }
    }

    override fun getItemCount(): Int = detailSchedules.size

    private fun dayConverter(day: String): String {
        when(day) {
            "MONDAY" -> return "월"
            "TUESDAY" -> return "화"
            "WEDNESDAY" -> return "수"
            "THURSDAY" -> return "목"
            "FRIDAY" -> return "금"
            "SATURDAY" -> return "토"
            "SUNDAY" -> return "일"
            else -> return ""
        }
    }

    private fun repeatDaysToKo(repeatDays: List<String>): String {
        var koDays = ""
        if(repeatDays.size == 7) koDays = "매일"
        else {
            koDays = repeatDays.map { dayConverter(it) }.joinToString(", ")
        }
        return koDays + " 반복"
    }

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
}

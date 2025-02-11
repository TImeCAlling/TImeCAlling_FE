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
    private var detailSchedules = ArrayList<ScheduleByDateResponseModel>()

    fun setScheduleList(scheduleList: ArrayList<ScheduleByDateResponseModel>) {
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
        holder.repeatInfo.text = fromEnToKo(detailSchedules[position].repeatDays?.get(0)) ?: ""
        holder.category.text = detailSchedules[position].categories[0].categoryName //카테고리 색 반영 나중에

        if(position == 0) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_28_mint200_fill_mint_line_1)
            holder.time.setTextColor(holder.time.context.getColor(R.color.mint_600))
        }

        if(position == detailSchedules.size -1) holder.bar.visibility = View.GONE

        //----------------- 아래 부분은 api 연결 확인 후에 ----------//
        /*holder.checkBtn.setOnClickListener {
            detailSchedules[position].isChecked = !detailSchedules[position].isChecked
            holder.checkBtn.setImageResource(if(detailSchedules[position].isChecked) R.drawable.ic_schedule_detail_check_mint else R.drawable.ic_schedule_detail_check)
        }*/

        /*//일정에 참여중인 인원이 3명 이상일 때
        if(detailSchedules[position].memberCount >= 3) {
            holder.extraMembersCount.text = "+${detailSchedules[position].memberCount - 2}"
        }
        //3명 미만
        else {
            holder.extraMembers.visibility = ViewGroup.GONE
            holder.memberSecond.layoutParams = (holder.memberSecond.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginEnd = 0
            }
        }*/

        holder.itemView.setOnClickListener {
            onItemClick.invoke(detailSchedules[position])
        }
    }

    override fun getItemCount(): Int = detailSchedules.size

    private fun fromEnToKo(en: String?) : String{
        var value=""
        when(en) {
            "SUNDAY" -> value = "일요일"
            "MONDAY" -> value = "월요일"
            "TUESDAY" -> value = "화요일"
            "WEDNESDAY" -> value = "수요일"
            "THURSDAY" -> value = "목요일"
            "FRIDAY" -> value = "금요일"
            "SATURDAY" -> value = "토요일"
        }
        return "매주 $value 반복"
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

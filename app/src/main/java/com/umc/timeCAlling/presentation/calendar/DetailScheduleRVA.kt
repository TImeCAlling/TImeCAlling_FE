package com.umc.timeCAlling.presentation.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.databinding.ItemTodayScheduleDetailBinding

class DetailScheduleRVA(
    private val detailSchedules: List<DetailSchedule>
) : RecyclerView.Adapter<DetailScheduleRVA.DetailScheduleViewHolder>() {
    inner class DetailScheduleViewHolder(private val binding: ItemTodayScheduleDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvDetailScheduleTitle
        val repeatInfo = binding.tvDetailScheduleRepeatInfo
        val category = binding.tvDetailScheduleCategory
        val time = binding.tvDetailScheduleTime
        val timeType = binding.tvDetailScheduleTimeType

        val memberFirst = binding.imgDetailScheduleMemberFirst
        val memberSecond = binding.imgDetailScheduleMemberSecond
        val extraMembers = binding.layoutDetailScheduleExtraMembers
        val extraMembersCount = binding.tvDetailScheduleExtraMembers

        val view = binding.viewDetailSchedule
        val container = binding.layoutDetailScheduleContainer
        val checkBtn = binding.imgDetailScheduleCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailScheduleViewHolder {
        val view = ItemTodayScheduleDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailScheduleViewHolder, position: Int) {
        holder.title.text = detailSchedules[position].title
        holder.repeatInfo.text = detailSchedules[position].repeatInfo
        holder.category.text = detailSchedules[position].category
        holder.time.text = detailSchedules[position].time
        holder.timeType.text = if(detailSchedules[position].isMorning) "오전" else "오후"

        if(position == 0) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_28_mint200_fill_mint_line_1)
            holder.time.setTextColor(holder.time.context.getColor(R.color.mint_600))
        }

        holder.checkBtn.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${detailSchedules[position].title} Clicked!", Toast.LENGTH_SHORT).show()
            detailSchedules[position].isChecked = !detailSchedules[position].isChecked
            holder.checkBtn.setImageResource(if(detailSchedules[position].isChecked) R.drawable.ic_schedule_detail_check_mint else R.drawable.ic_schedule_detail_check)
        }

        //일정에 참여중인 인원이 3명 이상일 때
        if(detailSchedules[position].memberCount >= 3) {
            holder.extraMembersCount.text = "+${detailSchedules[position].memberCount - 2}"
        }
        //3명 미만
        else {
            holder.extraMembers.visibility = ViewGroup.GONE
            holder.memberSecond.layoutParams = (holder.memberSecond.layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginEnd = 0
            }
        }
    }

    override fun getItemCount(): Int = detailSchedules.size
}

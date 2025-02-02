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
        holder.time.text = items[position].meetTime
        holder.timeLeft.text = ""
        holder.timeType.text = ""

        //제일 가까운 일정 ( 첫번째 인덱스 )는 민트색
        if(position == 0) {
            holder.view.setBackgroundResource(R.drawable.shape_rect_999_mint_fill)
            holder.container.setBackgroundResource(R.drawable.shape_rect_16_mint_fill_mint_line_1)
            holder.timeLeftContainer.setBackgroundResource(R.drawable.shape_rect_999_white_fill)
            holder.more.setImageResource(R.drawable.ic_home_more)
            holder.time.setTextColor(holder.timeType.context.getColor(R.color.mint_main))
            holder.description.setTextColor(holder.timeType.context.getColor(R.color.white))
        }

        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.more.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${items[position].name} Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size
}
package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.LocationResultType
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel

class LocationResultDetailRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LifecycleOwner,
    private val type: LocationResultType
) : RecyclerView.Adapter<LocationResultDetailRVA.LocationResultDetailViewHolder>() {

    class LocationResultDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbol = itemView.findViewById<ImageView>(R.id.iv_location_result_detail_icon)
        val transport = itemView.findViewById<TextView>(R.id.tv_location_result_detail_transport)
        val time = itemView.findViewById<TextView>(R.id.tv_location_result_detail_time)
        val arrow = itemView.findViewById<ImageView>(R.id.iv_location_result_detail_arrow)
        val circle = itemView.findViewById<View>(R.id.view_location_result_circle)
        val rect = itemView.findViewById<View>(R.id.view_location_result_rect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationResultDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_result_detail, parent, false)
        return LocationResultDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationResultDetailViewHolder, position: Int) {
        if (type == LocationResultType.Public) {
            viewModel.publicResult.observe(lifecycleOwner) { publicResult ->
                val legs = publicResult.metaData?.plan?.itineraries?.get(0)?.legs
                val leg = legs?.get(position) // position 번째 경로 정보 가져오기

                when (leg?.mode) {
                    "WALK" -> {
                        holder.symbol.setImageResource(R.drawable.ic_walk) // 걷기 아이콘 설정
                        holder.transport.text = "도보"
                    }
                    "BUS" -> {
                        holder.symbol.setImageResource(R.drawable.ic_public_transportation) // 지하철 아이콘 설
                        val route = leg?.route?.replace(": ", "") // "일반: " 제거
                        holder.transport.text = "${route}번"
                    }
                    "SUBWAY" -> {
                        holder.symbol.setImageResource(R.drawable.ic_train) // 지하철 아이콘 설정
                        holder.transport.text = "${leg?.route}"
                    }
                    else -> {
                    }
                }
                val totalMinute= leg?.sectionTime?.div(60)
                holder.time.text = "${totalMinute}분" // time 설정
            }
        }

        if (position < itemCount - 1) {
            holder.arrow.visibility = View.VISIBLE
            holder.circle.visibility = View.GONE
            holder.rect.visibility = View.GONE
        } else {
            holder.arrow.visibility = View.GONE
            holder.circle.visibility = View.VISIBLE
            holder.rect.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return if (type == LocationResultType.Public) {
            viewModel.publicResult.value?.metaData?.plan?.itineraries?.get(0)?.legs?.size ?: 0
        } else {
            0
        }
    }
}
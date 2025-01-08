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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationResultDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_result_detail, parent, false)
        return LocationResultDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationResultDetailViewHolder, position: Int) {
        Log.d("로그", "로그심기임")
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
                        holder.symbol.setImageResource(R.drawable.ic_public_transportation) // 버스 아이콘 설정
                        holder.transport.text = "${leg?.route}"
                    }
                    "SUBWAY" -> {
                        holder.symbol.setImageResource(R.drawable.ic_train) // 지하철 아이콘 설정
                        holder.transport.text = "${leg?.route}"
                    }
                    else -> {
                    }
                }

                holder.time.text = "${leg?.sectionTime}분" // time 설정
            }
        }
        Log.d("로그", "로그심기ㅎㅎ")

    }

    override fun getItemCount(): Int {
        return if (type == LocationResultType.Public) {
            viewModel.publicResult.value?.metaData?.plan?.itineraries?.get(0)?.legs?.size ?: 0
        } else {
            0
        }
    }
}
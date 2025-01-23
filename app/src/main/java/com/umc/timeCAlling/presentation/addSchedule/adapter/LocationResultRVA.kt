package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.LocationResultType
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel


class LocationResultRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LifecycleOwner,
    private val type: LocationResultType
) : RecyclerView.Adapter<LocationResultRVA.LocationResultViewHolder>() {

    private var _locationResultDetailRVA: LocationResultDetailRVA? = null
    private val locationResultDetailRVA get() = _locationResultDetailRVA

    init {
        if (type == LocationResultType.Public) {
            viewModel.publicResult.observe(lifecycleOwner) { publicResult ->
                notifyDataSetChanged() // 데이터 변경 알림
            }
        }
    }

    class LocationResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.tv_item_location_result_time)
        val distance = itemView.findViewById<TextView>(R.id.tv_item_location_result_distance)
        val symbol = itemView.findViewById<ImageView>(R.id.iv_item_location_result_symbol)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_location_result_details)
        val linearLayout = itemView.findViewById<ViewGroup>(R.id.layout_item_location_result_distance)
        var isSelected = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_result, parent, false)
        return LocationResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationResultViewHolder, position: Int) {
        when (type) {
            LocationResultType.Public -> {
                viewModel.publicResult.observe(lifecycleOwner) { publicResult ->
                    val totalTime =
                        publicResult.metaData?.plan?.itineraries?.get(position)?.totalTime
                    val totalMinutes = totalTime?.div(60)
                    holder.time.text = "${totalMinutes}분"
                    holder.recyclerView.visibility = View.VISIBLE
                    holder.linearLayout.visibility = View.GONE

                    Log.d("로그", "로그심기 빵야")
                    _locationResultDetailRVA = LocationResultDetailRVA(
                        viewModel = viewModel,
                        lifecycleOwner = lifecycleOwner,
                        viewLifecycleOwner = viewLifecycleOwner,
                        type = LocationResultType.Public
                    )
                    Log.d("로그", "ㅎㅇ")
                    holder.recyclerView.adapter = locationResultDetailRVA
                    holder.recyclerView.layoutManager =
                        LinearLayoutManager(holder.recyclerView.context)
                    holder.recyclerView.setLayoutManager(
                        LinearLayoutManager(
                            holder.recyclerView.context,
                            RecyclerView.HORIZONTAL,
                            false
                        )
                    )
                    holder.recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            holder.itemView.context,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                }
            }
            LocationResultType.Car -> {
                Log.d("로그", "로그심기")
                viewModel.carResult.observe(lifecycleOwner) { carResult ->
                    val totalTime = carResult.features?.get(0)?.properties?.totalTime
                    val totalMinutes = totalTime?.div(60)
                    holder.symbol.setImageResource(R.drawable.ic_car)
                    val totalDistance = carResult.features?.get(0)?.properties?.totalDistance
                    val totalKilometers = totalDistance?.div(1000)
                    holder.distance.text = "${totalKilometers}km"
                    holder.time.text = "${totalMinutes}분"
                    holder.recyclerView.visibility = View.GONE
                    holder.linearLayout.visibility = View.VISIBLE
                    Log.d("로그", "로그심기 뿡")
                }
            }
            LocationResultType.Walk -> {
                viewModel.walkResult.observe(lifecycleOwner) { walkResult ->
                    val totalTime = walkResult.features?.get(0)?.properties?.totalTime
                    val totalMinutes = totalTime?.div(60)
                    val totalHours = totalMinutes?.div(60)
                    val remainingMinutes = totalMinutes?.rem(60)
                    holder.symbol.setImageResource(R.drawable.ic_walk)
                    val totalDistance = walkResult.features?.get(0)?.properties?.totalDistance
                    val totalKilometers = totalDistance?.div(1000)
                    holder.distance.text = "${totalKilometers}km"
                    holder.time.text = "${totalHours}시간 ${remainingMinutes}분"
                    holder.recyclerView.visibility = View.GONE
                    holder.linearLayout.visibility = View.VISIBLE
                }
            }
        }

        holder.itemView.setOnClickListener {
            holder.isSelected = !holder.isSelected
            holder.itemView.setBackgroundResource(R.drawable.shape_rect_16_white_fill_mint_line_1)

            // 선택된 아이템의 시간 가져오기
            val selectedTime = when (type) {
                LocationResultType.Public -> {
                    viewModel.publicResult.value?.metaData?.plan?.itineraries?.get(position)?.totalTime?.div(60)
                }
                LocationResultType.Car -> {
                    viewModel.carResult.value?.features?.get(0)?.properties?.totalTime?.div(60)
                }
                LocationResultType.Walk -> {
                    viewModel.walkResult.value?.features?.get(0)?.properties?.totalTime?.div(60)
                }
            }

            // ViewModel에 선택된 시간 저장
            selectedTime?.let { viewModel.setMoveTime(it) }

            // 배경색 업데이트
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        val count = when (type) {
            LocationResultType.Public -> viewModel.publicResult.value?.metaData?.plan?.itineraries?.size ?: 0
            LocationResultType.Car -> 1
            LocationResultType.Walk -> 1
        }
        Log.d("LocationResultRVA", "getItemCount: type=$type, count=$count") // 로그 추가
        return count
    }
}

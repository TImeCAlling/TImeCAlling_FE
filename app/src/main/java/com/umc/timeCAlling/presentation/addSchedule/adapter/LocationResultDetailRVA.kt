package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel

class LocationResultDetailRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<LocationResultDetailRVA.LocationResultDetailViewHolder>() {

    class LocationResultDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationResultDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_result_detail, parent, false)
        return LocationResultDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationResultDetailViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}

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

class LocationResultRVA(
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<LocationResultRVA.LocationResultViewHolder>() {

    class LocationResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_result, parent, false)
        return LocationResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationResultViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}

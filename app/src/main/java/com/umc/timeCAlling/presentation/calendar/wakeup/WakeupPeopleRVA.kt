package com.umc.timeCAlling.presentation.calendar.wakeup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R

class WakeupPeopleRVA (): RecyclerView.Adapter<WakeupPeopleRVA.WakeupPeopleViewHolder>() {

    class WakeupPeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WakeupPeopleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wakeup_people, parent, false)
        return WakeupPeopleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WakeupPeopleViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }

}
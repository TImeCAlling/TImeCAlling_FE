package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R

class CategoryEditRVA(): RecyclerView.Adapter<CategoryEditRVA.CategoryEditViewHolder>() {

    class CategoryEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEditViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryEditViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryEditViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }

}
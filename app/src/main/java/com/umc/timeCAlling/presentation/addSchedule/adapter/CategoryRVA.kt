package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager

class CategoryRVA(
    private val context: Context, // Context 추가
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController
): RecyclerView.Adapter<CategoryRVA.CategoryViewHolder>() {

    private val categories = CategoryManager.getCategories()
    private var selectedCategoryPosition: Int = -1

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        val ivCategoryLogo: ImageView = itemView.findViewById(R.id.iv_category_logo)
        val ivCategorySelect: ImageView = itemView.findViewById(R.id.iv_category_select)
        val constraintLayout: LinearLayout = itemView.findViewById(R.id.layout_category_item)
    }

    init {
        CategoryManager.loadCategories(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_sheet_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.tvCategoryName.text = category.name
        holder.ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,  CategoryManager.getColor(category.color)))

        holder.itemView.setOnClickListener {
            viewModel.setCategoryName(category.name)
            notifyDataSetChanged() // RecyclerView 업데이트
        }

        // Update UI for selected category
        if (selectedCategoryPosition == position) {
            holder.ivCategorySelect.setImageResource(R.drawable.ic_circle_check_mint)
            holder.itemView.setBackgroundResource(R.drawable.shape_rect_16_white_fill_mint_line_1_none_shadow)
        } else {
            holder.ivCategorySelect.setImageResource(R.drawable.ic_circle_check)
            holder.itemView.setBackgroundResource(R.drawable.shape_rect_16_white_fill_shadow)
        }

        holder.itemView.setOnClickListener {
            selectedCategoryPosition = holder.adapterPosition
            viewModel.setCategoryName(category.name)
            notifyDataSetChanged() // RecyclerView 업데이트
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
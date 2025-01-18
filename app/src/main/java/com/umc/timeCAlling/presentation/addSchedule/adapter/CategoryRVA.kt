package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.semantics.text
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category
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

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    init {
        CategoryManager.loadCategories(context) // 초기화 시 데이터 로드
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_sheet_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        val tvCategoryName = holder.itemView.findViewById<TextView>(R.id.tv_category_name)
        val ivCategoryLogo = holder.itemView.findViewById<ImageView>(R.id.iv_category_logo)
        val ivCategorySelect = holder.itemView.findViewById<ImageView>(R.id.iv_category_select)
        val constraintLayout = holder.itemView.findViewById<LinearLayout>(R.id.layout_category_item)

        tvCategoryName.text = category.name
        ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, category.color))

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            viewModel.selectedCategory.value = category.name // ViewModel에 선택된 카테고리 업데이트
            notifyDataSetChanged() // RecyclerView 업데이트
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
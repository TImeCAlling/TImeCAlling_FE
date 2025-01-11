package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager.deleteCategory

class CategoryEditRVA(
    private val context: Context, // Context 추가
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController
): RecyclerView.Adapter<CategoryEditRVA.CategoryEditViewHolder>() {

    class CategoryEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    init {
        CategoryManager.loadCategories(context) // 초기화 시 데이터 로드
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEditViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryEditViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryEditViewHolder, position: Int) {
        val itemView = holder.itemView
        val ivCategoryMore = itemView.findViewById<ImageView>(R.id.iv_category_more)
        val etCategoryName = itemView.findViewById<EditText>(R.id.et_category_name)
        val ivCategoryLogo = itemView.findViewById<ImageView>(R.id.iv_category_logo)
        val layoutCategoryColor = itemView.findViewById<GridLayout>(R.id.layout_category_color)
        val category = CategoryManager.getCategories()[position]

        ivCategoryLogo.setColorFilter(ContextCompat.getColor(itemView.context, category.color))
        ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, category.color))

        etCategoryName.setText(category.name)
        etCategoryName.isEnabled = false // 초기에는 수정 불가능하도

        val colorViews = arrayOf(
            itemView.findViewById<View>(R.id.view_category_orange),
            itemView.findViewById<View>(R.id.view_category_yellow),
            itemView.findViewById<View>(R.id.view_category_green),
            itemView.findViewById<View>(R.id.view_category_mint),
            itemView.findViewById<View>(R.id.view_category_skyblue),
            itemView.findViewById<View>(R.id.view_category_pink),
            itemView.findViewById<View>(R.id.view_category_mauve),
            itemView.findViewById<View>(R.id.view_category_purple),
            itemView.findViewById<View>(R.id.view_category_blue),
            itemView.findViewById<View>(R.id.view_category_gray)
        )

        val colorResources = arrayOf(
            R.color.category_orange,
            R.color.category_yellow,
            R.color.category_green,
            R.color.mint_main,
            R.color.category_skyblue,
            R.color.category_pink,
            R.color.category_mauve,
            R.color.category_purple,
            R.color.category_blue,
            R.color.gray_600
        )

        for (i in colorViews.indices) {
            colorViews[i].setOnClickListener {
                // 로고 색상 변경 및 CategoryManager 업데이트
                ivCategoryLogo.setColorFilter(ContextCompat.getColor(itemView.context, colorResources[i]))
                ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, colorResources[i]))
                val updatedCategory = category.copy(color = colorResources[i])
                CategoryManager.updateCategory(position, updatedCategory)
                layoutCategoryColor.visibility = View.GONE
            }
        }

        ivCategoryLogo.setOnClickListener {
            if (etCategoryName.isEnabled) { // 수정하기 선택된 경우에만 color 팔레트 표시
                layoutCategoryColor.visibility = if (layoutCategoryColor.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
        }

        ivCategoryMore.setOnClickListener {
            PopupMenu(itemView.context, it, Gravity.END, 0, R.style.PopupMenuStyle).apply {
                menuInflater.inflate(R.menu.menu_category_edit, menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.category_edit -> {
                            etCategoryName.isEnabled = true
                            etCategoryName.requestFocus()
                            layoutCategoryColor.visibility = View.VISIBLE
                            true
                        }
                        R.id.category_delete -> {
                            CategoryManager.deleteCategory(position)
                            notifyItemRemoved(position) // 아이템 삭제 알림
                            true
                        }
                        else -> false
                    }
                }
                setForceShowIcon(true)
            }.show()
        }

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        CategoryManager.saveCategories(context) // RecyclerView에서 detach될 때 데이터 저장
    }

    override fun getItemCount(): Int {
        return CategoryManager.getCategories().size // CategoryManager의 리스트 크기 반환
    }
}
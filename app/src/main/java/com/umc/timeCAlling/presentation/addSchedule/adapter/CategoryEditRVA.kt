package com.umc.timeCAlling.presentation.addSchedule.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category
import com.umc.timeCAlling.presentation.addSchedule.AddScheduleViewModel
import com.umc.timeCAlling.presentation.addSchedule.CategoryManager

class CategoryEditRVA(
    private val context: Context, // Context 추가
    private val viewModel: AddScheduleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navController: NavController
): RecyclerView.Adapter<CategoryEditRVA.CategoryEditViewHolder>() {

    private var modifiedCategories: MutableList<Category> = CategoryManager.getCategories().toMutableList()

    class CategoryEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoryMore: ImageView = itemView.findViewById(R.id.iv_category_more)
        val etCategoryName: EditText = itemView.findViewById(R.id.et_category_name)
        val ivCategoryLogo: ImageView = itemView.findViewById(R.id.iv_category_logo)
        val layoutCategoryColor: GridLayout = itemView.findViewById(R.id.layout_category_color)
        val colorViews: Array<View> = arrayOf(
            itemView.findViewById(R.id.view_category_orange),
            itemView.findViewById(R.id.view_category_yellow),
            itemView.findViewById(R.id.view_category_mint),
            itemView.findViewById(R.id.view_category_skyblue),
            itemView.findViewById(R.id.view_category_blue),
            itemView.findViewById(R.id.view_category_green),
            itemView.findViewById(R.id.view_category_pink),
            itemView.findViewById(R.id.view_category_mauve),
            itemView.findViewById(R.id.view_category_purple),
            itemView.findViewById(R.id.view_category_gray)
        )
        val colorResources: Array<Int> = arrayOf(
            CategoryManager.getColor(0),
            CategoryManager.getColor(1),
            CategoryManager.getColor(2),
            CategoryManager.getColor(3),
            CategoryManager.getColor(4),
            CategoryManager.getColor(5),
            CategoryManager.getColor(6),
            CategoryManager.getColor(7),
            CategoryManager.getColor(8),
            CategoryManager.getColor(9)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEditViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryEditViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryEditViewHolder, position: Int) {
        val itemView = holder.itemView
        val category = modifiedCategories[position]

        val ivCategoryMore = holder.ivCategoryMore
        val etCategoryName = holder.etCategoryName
        val ivCategoryLogo = holder.ivCategoryLogo
        val layoutCategoryColor = holder.layoutCategoryColor
        val colorViews = holder.colorViews
        val colorResources = holder.colorResources

        holder.ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, category.color))

        etCategoryName.setText(category.name)
        etCategoryName.isEnabled = false

        etCategoryName.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etCategoryName.isEnabled) {
                    val updatedCategory = Category(etCategoryName.text.toString(), category.color)
                    modifiedCategories[position] = updatedCategory
                    CategoryManager.updateCategory(position, updatedCategory, context)
                    ivCategoryLogo.setColorFilter(category.color) // ContextCompat.getColor() 제거
                    ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(category.color) // ContextCompat.getColor() 제거
                    notifyItemChanged(position)
                    layoutCategoryColor.visibility = View.GONE
                    etCategoryName.isEnabled = false
                }
            }
            false
        }

        colorViews.forEachIndexed { index, view ->
            val colorResource = colorResources[index]
            view.setOnClickListener {
                if (etCategoryName.isEnabled) {
                    val updatedCategory = Category(etCategoryName.text.toString(), colorResource)
                    modifiedCategories[position] = updatedCategory
                    CategoryManager.updateCategory(position, updatedCategory, context)
                    ivCategoryLogo.setColorFilter(colorResource) // ContextCompat.getColor() 제거
                    ivCategoryLogo.backgroundTintList = ColorStateList.valueOf(colorResource) // ContextCompat.getColor() 제거
                    notifyItemChanged(position)
                    layoutCategoryColor.visibility = View.GONE
                }
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
                            val deletedCategory = modifiedCategories.removeAt(position)
                            CategoryManager.deleteCategory(position, context)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, itemCount)
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
        CategoryManager.saveCategories(context)
    }

    override fun getItemCount(): Int {
        return modifiedCategories.size // CategoryManager의 리스트 크기 반환
    }

    fun updateCategories(newCategories: MutableList<Category>) {
        modifiedCategories = newCategories.toMutableList()
        notifyDataSetChanged()
    }
}
package com.umc.timeCAlling.presentation.addSchedule

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category
import com.umc.timeCAlling.databinding.FragmentCalendarBinding
import com.umc.timeCAlling.databinding.FragmentCategoryEditBinding
import com.umc.timeCAlling.presentation.addSchedule.adapter.CategoryEditRVA
import com.umc.timeCAlling.presentation.addSchedule.adapter.CategoryRVA
import com.umc.timeCAlling.presentation.base.BaseFragment
import com.umc.timeCAlling.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryEditFragment: BaseFragment<FragmentCategoryEditBinding>(R.layout.fragment_category_edit) {

    private val viewModel: AddScheduleViewModel by activityViewModels() // ViewModel 초기화
    private lateinit var categoryEditRVA: CategoryEditRVA

    override fun initView() {

        bottomNavigationRemove()
        initCategoryList()

        binding.ivCalendarEditBack.setOnSingleClickListener {
            findNavController().popBackStack()
        }

    }

    override fun initObserver() {

    }

    private fun bottomNavigationRemove() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.main_bnv)
        bottomNavigationView?.visibility = View.GONE

        val addScheduleButton = requireActivity().findViewById<View>(R.id.iv_main_add_schedule_btn)
        addScheduleButton?.visibility = View.GONE

        val shadowImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_shadow)
        shadowImageView?.visibility = View.GONE

        val ovalImageView = requireActivity().findViewById<View>(R.id.iv_main_bnv_white_oval)
        shadowImageView?.visibility = View.GONE
        ovalImageView?.visibility = View.GONE
    }

    private fun initCategoryList() {
         categoryEditRVA = CategoryEditRVA(
            requireContext(),
            viewModel,
            viewLifecycleOwner,
            viewLifecycleOwner,
            findNavController()
        )
        binding.rvCategoryEdit.adapter = categoryEditRVA
        binding.rvCategoryEdit.layoutManager = LinearLayoutManager(requireContext())
        Log.d("", "결과")
        CategoryManager.loadCategories(requireContext())

        binding.ivCategoryPlus.setOnClickListener {
            val newCategory = Category("새로운 카테고리", 9)
            val lastPosition = categoryEditRVA.itemCount
            CategoryManager.addCategory(newCategory, requireContext())
            categoryEditRVA.updateCategories(CategoryManager.getCategories().toMutableList())
            categoryEditRVA.notifyItemInserted(lastPosition)

            val viewHolder = binding.rvCategoryEdit.findViewHolderForAdapterPosition(lastPosition) as? CategoryEditRVA.CategoryEditViewHolder
            viewHolder?.let {
                val editText = it.itemView.findViewById<EditText>(R.id.et_category_name)
                editText.isEnabled = true
                editText.requestFocus()
            }
        }
    }
}
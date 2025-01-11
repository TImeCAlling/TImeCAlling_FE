package com.umc.timeCAlling.presentation.addSchedule

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category

object CategoryManager {
    private const val PREFS_NAME = "category_prefs"
    private const val KEY_CATEGORIES = "categories"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun loadCategories(context: Context) {
        val prefs = getSharedPreferences(context)
        val categoriesJson = prefs.getString(KEY_CATEGORIES, null)
        if (categoriesJson != null) {
            val type = object : TypeToken<List<Category>>() {}.type
            categories.addAll(Gson().fromJson(categoriesJson, type))
        }
    }

    private val categories = mutableListOf(
        Category("일상", R.color.category_orange),
        Category("학교", R.color.category_yellow),
    )
    fun getCategories(): List<Category> = categories

    fun addCategory(category: Category) {
        categories.add(category)
    }

    fun deleteCategory(position: Int) {
        categories.removeAt(position)
    }

    fun updateCategory(position: Int, updatedCategory: Category) {
        if (position in 0 until categories.size) {
            categories[position] = updatedCategory
        }
    }

    fun saveCategories(context: Context) {
        val prefs = getSharedPreferences(context)
        val categoriesJson = Gson().toJson(categories)
        prefs.edit().putString(KEY_CATEGORIES, categoriesJson).apply()
    }

}
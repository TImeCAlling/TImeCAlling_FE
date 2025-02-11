package com.umc.timeCAlling.presentation.addSchedule

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.input.key.type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.timeCAlling.R
import com.umc.timeCAlling.data.Category
import kotlin.collections.addAll
import java.lang.reflect.Type

object CategoryManager {
    private const val PREFS_NAME = "category_prefs" // SharedPreferences 파일 이름
    private const val KEY_CATEGORIES = "categories" // 카테고리 목록을 저장할 키

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getColor(color:Int):Int {
        return when (color){
            0 -> R.color.category_orange
            1 -> R.color.category_yellow
            2 -> R.color.mint_main
            3 -> R.color.category_skyblue
            4 -> R.color.category_blue
            5 -> R.color.category_green
            6 -> R.color.category_pink
            7 -> R.color.category_mauve
            8 -> R.color.category_purple
            9 -> R.color.gray_600
            else -> R.color.white
        }
    }

    fun loadCategories(context: Context) {
        val prefs = getSharedPreferences(context)
        val categoriesJson = prefs.getString(KEY_CATEGORIES, null)

        if (categoriesJson != null) {
            val type: Type = object : TypeToken<List<Category>>() {}.type
            val loadedCategories: List<Category> = Gson().fromJson(categoriesJson, type)
            categories.clear()
            categories.addAll(loadedCategories)
        } else {
            categories.clear()
            categories.addAll(
                listOf(
                    Category("일상", 0),
                    Category("학교", 1),
                    Category("알바",6),
                    Category("공부", 3),
                )
            )
            saveCategories(context)
        }
    }

    fun getCategoryByName(categoryName: String): Category? {
        return categories.find { it.name == categoryName }
    }

    fun saveCategories (context: Context) {
        val prefs = getSharedPreferences(context)
        val categoriesJson = Gson().toJson(categories)
        prefs.edit().putString(KEY_CATEGORIES, categoriesJson).apply()
    }

    private val categories = mutableListOf<Category>()
    fun getCategories(): List<Category> = categories

    fun addCategory(category: Category, context: Context) {
        categories.add(category)
        saveCategories(context)
    }

    fun deleteCategory(position: Int, context: Context) {
        categories.removeAt(position)
        saveCategories(context)
    }

    fun updateCategory(position: Int, updatedCategory: Category, context: Context) {
        if (position in 0 until categories.size) {
            categories[position] = updatedCategory
            saveCategories(context)
        }
    }

    fun clearCategories() {
        categories.clear()
    }
}
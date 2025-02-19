package com.umc.timeCAlling.presentation.home.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChecklistSharedPreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ChecklistPrefs", Context.MODE_PRIVATE)
    private val prefs2: SharedPreferences = context.getSharedPreferences("DeletedChecklistPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveList(key: String, list: List<Int>) {
        val jsonString = gson.toJson(list)
        prefs.edit().putString(key, jsonString).apply()
    }

    fun getList(key: String): List<Int> {
        val jsonString = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun deleteItem(key: String, item: Int) {
        val list = getList(key).toMutableList()
        list.remove(item)
        saveList(key, list)
    }

    fun clearList(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun saveDeletedList(key: String, list: List<Int>) {
        val jsonString = gson.toJson(list)
        prefs2.edit().putString(key, jsonString).apply()
    }

    fun getDeletedList(key: String): List<Int> {
        val jsonString = prefs2.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun clearDeletedList(key: String) {
        prefs2.edit().remove(key).apply()
    }

}
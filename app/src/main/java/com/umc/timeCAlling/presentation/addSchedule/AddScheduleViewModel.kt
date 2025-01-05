package com.umc.timeCAlling.presentation.addSchedule

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.remove
import kotlin.collections.toMutableList

@HiltViewModel
class AddScheduleViewModel @Inject constructor( // @Inject : 의존성 주입을 받겠다.
    private val spf: SharedPreferences
    ) : ViewModel() {

    private val maxRecentSearches = 10 // 최대 검색어 개수
    private val _recentSearches = MutableLiveData<List<String>>(emptyList())
    val recentSearches: LiveData<List<String>> = _recentSearches

    init {
        _recentSearches.value = loadRecentSearches() // 초기화 시 로드
    }

    fun addRecentSearch(search: String) {
        val updatedSearches = _recentSearches.value?.toMutableList()?.also {
            it.remove(search) // 중복된 검색어 제거
            it.add(0, search) // 최근 검색어를 맨 앞에 추가
            if (it.size > maxRecentSearches) {
                it.removeAt(it.size - 1) // 최대 개수 초과 시 오래된 검색어 삭제
            }
        }
        _recentSearches.value = updatedSearches ?: listOf(search)
        saveRecentSearches(_recentSearches.value!!) // 저장
    }

    fun deleteRecentSearch(search: String) {
        val updatedSearches = _recentSearches.value?.toMutableList()?.also { it.remove(search) }
        _recentSearches.value = updatedSearches ?: emptyList()
        saveRecentSearches(_recentSearches.value!!) // 저장
    }

    private fun loadRecentSearches(): List<String> {
        val searchesJson = spf.getString("searches", null)
        return if (searchesJson != null) {
            Gson().fromJson(searchesJson, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList()
        }
    }

    private fun saveRecentSearches(searches: List<String>) {
        val searchesJson = Gson().toJson(searches)
        spf.edit().putString("searches", searchesJson).apply()
    }
}
package com.umc.timeCAlling.presentation.addSchedule

import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.timeCAlling.data.SearchResult
import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel
import com.umc.timeCAlling.domain.repository.TmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toMutableList

@HiltViewModel
class AddScheduleViewModel @Inject constructor( // @Inject : 의존성 주입을 받겠다.
    private val spf: SharedPreferences,
    private val repository: TmapRepository
    ) : ViewModel() {

    private val _categoryNeedsRefresh = MutableStateFlow<String>("대중교통")
    val categoryNeedsRefresh: StateFlow<String> get() = _categoryNeedsRefresh

    private val maxRecentSearches = 10 // 최대 검색어 개수

    private val _recentSearches = MutableLiveData<List<String>>(emptyList())
    val recentSearches: LiveData<List<String>> = _recentSearches

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    private val _carResult = MutableLiveData<CarTransportationModel>()
    val carResult: LiveData<CarTransportationModel> = _carResult

    private val _walkResult = MutableLiveData<WalkTransportationModel>()
    val walkResult: LiveData<WalkTransportationModel> = _walkResult

    private val _publicResult = MutableLiveData<PublicTransportationModel>()
    val publicResult: LiveData<PublicTransportationModel> = _publicResult

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

    fun updateSearchResults(results: List<SearchResult>) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            _searchResults.value = results // UI 스레드에서 호출되는 경우
        } else {
            _searchResults.postValue(results) // 다른 스레드에서 호출되는 경우
        }
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

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun refreshCategoryPage(category: String){
        _categoryNeedsRefresh.value = ""
        _categoryNeedsRefresh.value = category
    }

    fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            repository.getCarTransportation(startX, startY, endX, endY).onSuccess { response ->
                _carResult.value = response // LiveData에 API 응답 저장
                Log.d("자동차 거리", "${response.features?.get(0)?.properties?.totalDistance}") // 로그에 거리 출력
                Log.d("자동차 시간", "${response.features?.get(0)?.properties?.totalTime}") // 로그에 거리 출력
            }.onFailure { error ->
                Log.e("자동차 오류", "API 호출 실패: $error")
            }
        }
    }

    fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            repository.getWalkTransportation(startX, startY, endX, endY).onSuccess { response ->
                _walkResult.value = response // LiveData에 API 응답 저장
                Log.d("걷기 거리", "${response.features?.get(0)?.properties?.totalDistance}") // 로그에 거리 출력
                Log.d("걷기 시간", "${response.features?.get(0)?.properties?.totalTime}") // 로그에 거리 출력
            }.onFailure { error ->
                Log.e("걷기 오류", "API 호출 실패: $error")

            }
        }
    }

    fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double) {
        viewModelScope.launch {
            repository.getPublicTransportation(startX, startY, endX, endY).onSuccess { response ->
                _publicResult.value = response // LiveData에 API 응답 저장
                Log.d("대중교통 시간", "${response.metaData?.plan?.itineraries?.get(0)?.totalTime}")
            }.onFailure { error->
                Log.e("대중교통 오류", "API 호출 실패: $error")
            }
        }
    }
}
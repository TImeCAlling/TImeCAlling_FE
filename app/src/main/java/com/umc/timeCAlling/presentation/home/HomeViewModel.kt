package com.umc.timeCAlling.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _todaySchedules = MutableLiveData<List<TodayScheduleResponseModel>>()
    val todaySchedules: LiveData<List<TodayScheduleResponseModel>> get() = _todaySchedules

    private val _successRate = MutableLiveData<SuccessRateResponseModel>()
    val successRate: LiveData<SuccessRateResponseModel> get() = _successRate

    fun getTodaySchedules() {
        viewModelScope.launch {
            scheduleRepository.getTodaySchedules().onSuccess { response ->
                _todaySchedules.value = response.schedules.sortedBy { it.meetTime }
                Log.d("getTodaySchedules()" , response.toString())
            }.onFailure { error ->
                // 에러 처리
                Log.d("getTodaySchedules()" , error.toString())
            }
        }
    }

    fun getSuccessRate() {
        viewModelScope.launch {
            scheduleRepository.getSuccessRate().onSuccess { response ->
                _successRate.value = response
                Log.d("getSuccessRate()" , response.toString())
            }.onFailure { error ->
                // 에러 처리
                Log.d("getSuccessRate()" , error.toString())
            }
        }

    }
}
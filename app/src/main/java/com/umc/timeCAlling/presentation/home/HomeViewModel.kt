package com.umc.timeCAlling.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleStatusResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _todaySchedules = MutableLiveData<List<TodayScheduleResponseModel>>()
    val todaySchedules: LiveData<List<TodayScheduleResponseModel>> get() = _todaySchedules

    fun getTodaySchedules() {
        viewModelScope.launch {
            scheduleRepository.getTodaySchedules().onSuccess { response ->
                val sortedSchedules = response.schedules.sortedBy {it.meetTime}
                val upcomingSchedules = getUpcomingSchedules(sortedSchedules)
                _todaySchedules.value = sortedSchedules
                Log.d("getTodaySchedules()" , response.toString())
            }.onFailure { error ->
                // 에러 처리
                Log.d("getTodaySchedules()" , error.toString())
            }
        }
    }

    private fun getUpcomingSchedules(schedules: List<TodayScheduleResponseModel>): List<TodayScheduleResponseModel> {
        val now = LocalTime.now()
        val upcomingSchedules = schedules.filter { schedule ->
            val meetTime = LocalTime.parse(schedule.meetTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            meetTime.isAfter(now)
        }
        return upcomingSchedules
    }

    private val _successRate = MutableLiveData<SuccessRateResponseModel>()
    val successRate: LiveData<SuccessRateResponseModel> get() = _successRate
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

    private val _scheduleStatus = MutableLiveData<ScheduleStatusResponseModel>()
    val scheduleStatus: LiveData<ScheduleStatusResponseModel> get() = _scheduleStatus

    fun getScheduleStatus(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.getScheduleStatus(scheduleId).onSuccess { response ->
                _scheduleStatus.value = response
            }.onFailure { error ->
                Log.d("getScheduleStatus()" , error.toString())
            }
        }
    }
}
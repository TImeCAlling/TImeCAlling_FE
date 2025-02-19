package com.umc.timeCAlling.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleStatusResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import com.umc.timeCAlling.presentation.home.helper.ChecklistSharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.addAll

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    application: Application
) : ViewModel() {

    private val sharedPrefsHelper = ChecklistSharedPreferencesHelper(application)
    private val _checklists = MutableLiveData<List<Int>>()
    val checklists: LiveData<List<Int>> get() = _checklists
    private val _deletedId = MutableLiveData<List<Int>>()
    val deletedId: LiveData<List<Int>> get() = _deletedId

    private val _todaySchedules = MutableLiveData<List<TodayScheduleResponseModel>>()
    val todaySchedules: LiveData<List<TodayScheduleResponseModel>> get() = _todaySchedules

    private val _lastSchedules = MutableLiveData<List<TodayScheduleResponseModel>>()
    val lastSchedules: LiveData<List<TodayScheduleResponseModel>> get() = _lastSchedules

    fun getTodaySchedules() {
        viewModelScope.launch {
            scheduleRepository.getTodaySchedules().onSuccess { response ->
                val sortedSchedules = response.schedules.sortedBy {it.meetTime}
                val upcomingSchedules = getUpcomingSchedules(sortedSchedules)
                _todaySchedules.value = upcomingSchedules
                Log.d("getTodaySchedules()" , response.toString())
            }.onFailure { error ->
                // 에러 처리
                Log.d("getTodaySchedules()" , error.toString())
            }
        }
    }

    private fun getUpcomingSchedules(schedules: List<TodayScheduleResponseModel>): List<TodayScheduleResponseModel> {
        val now = LocalTime.now()
        val (last, upcomingSchedules) = schedules.partition { schedule ->
            val meetTime = LocalTime.parse(schedule.meetTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            meetTime.isBefore(now) || meetTime == now
        }
        last.forEach { schedule ->
            addItem(schedule.checkListId)
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

    fun loadItems() {
        _checklists.value = sharedPrefsHelper.getList("ChecklistPrefs")
    }

    fun addItem(item: Int) {
        val currentList = _checklists.value.orEmpty().toMutableList()
        val deletedList = _deletedId.value.orEmpty().toMutableList()
        if (!currentList.contains(item) && !deletedList.contains(item)) { // 중복 체크 & 삭제 확인
            currentList.add(item)
            sharedPrefsHelper.saveList("ChecklistPrefs", currentList)
            _checklists.value = currentList
        } else {
            Log.d("SharedViewModel", "이미 존재하는 값: $item")
        }
    }

    fun addDeletedItem(item: Int) {
        val currentList = _deletedId.value.orEmpty().toMutableList()
        if (!currentList.contains(item)) { // 중복 체크
            currentList.add(item)
            sharedPrefsHelper.saveDeletedList("DeletedChecklistPrefs", currentList)
            _deletedId.value = currentList
        } else {
            Log.d("SharedViewModel", "이미 존재하는 값: $item")
        }
    }

    fun clearAll() {
        sharedPrefsHelper.clearList("ChecklistPrefs")
        _checklists.value = emptyList()
    }

    fun clearDeletedAll() {
        sharedPrefsHelper.clearDeletedList("DeletedChecklistPrefs")
        _deletedId.value = emptyList()
    }
}
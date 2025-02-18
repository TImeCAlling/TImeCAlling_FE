package com.umc.timeCAlling.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import com.umc.timeCAlling.presentation.calendar.wakeup.WakeupPeopleRVA
import com.umc.timeCAlling.presentation.calendar.wakeup.WakeupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _schedules = MutableLiveData<List<ScheduleByDateResponseModel>>()
    val schedules: LiveData<List<ScheduleByDateResponseModel>> get() = _schedules

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _scheduleUsers = MutableLiveData<List<ScheduleUsersResponseModel>>()
    val scheduleUsers: LiveData<List<ScheduleUsersResponseModel>> get() = _scheduleUsers

    private val _scheduleId = MutableLiveData<Int>()
    val scheduleId: LiveData<Int> get() = _scheduleId

    fun getScheduleByDate(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            scheduleRepository.getScheduleByDate(date).onSuccess { response ->
                if (response.schedules.isNotEmpty()) {
                    val schedules = response.schedules.sortedBy{it.meetTime}
                    val upcomingSchedules = getUpcomingSchedules(schedules)
                    _schedules.value = schedules
                    Log.d("ScheduleViewModel", response.toString())
                } else {
                    _schedules.value = emptyList()
                    Log.d("ScheduleViewModel", "HTTP 요청은 성공했지만, 데이터가 비어있습니다.")
                }
            }.onFailure { error ->
                Log.e("ScheduleViewModel", "HTTP 요청 실패: $error")
            }.also {
                _isLoading.value = false // 로딩 종료
            }
        }
    }

    private fun getUpcomingSchedules(schedules: List<ScheduleByDateResponseModel>): List<ScheduleByDateResponseModel> {
        val now = LocalTime.now()
        val upcomingSchedules = schedules.filter { schedule ->
            val meetTime = LocalTime.parse(schedule.meetTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            meetTime.isAfter(now)
        }
        return upcomingSchedules
    }

    var isSharedSchedule: Boolean = false

    fun getScheduleUsers(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.getScheduleUsers(scheduleId).onSuccess { response ->
                if (response != null) {
                    Log.d("ScheduleViewModel", response.toString())
                    _scheduleUsers.value = response
                    // 공유 스케줄 여부 판단
                    isSharedSchedule = response.size > 1 // 참여자가 1명 초과면 공유 스케줄로 판단
                } else {
                    // 응답이 null인 경우, 빈 리스트를 할당하거나 오류 메시지를 표시
                    Log.e("ScheduleViewModel", "getScheduleUsers 응답이 null입니다.")
                    _scheduleUsers.value = emptyList()
                    isSharedSchedule = false // 응답이 null이면 공유 스케줄이 아님
                } }.onFailure { error ->
                Log.e("ScheduleViewModel", "HTTP 요청 실패: $error")
            }
        }
    }

    private val _detailSchedule = MutableLiveData<DetailScheduleResponseModel>()
    val detailSchedule: LiveData<DetailScheduleResponseModel> get() = _detailSchedule

    private val _shareId = MutableLiveData<String>()
    val shareId: LiveData<String> get() = _shareId

    private val _meetDate = MutableLiveData<String>()
    val meetDate: LiveData<String> get() = _meetDate

    fun getDetailSchedule(checklistId: Int) {
        viewModelScope.launch {
            scheduleRepository.getDetailSchedule(checklistId).onSuccess { response ->
                _detailSchedule.value = response
                _scheduleId.value = response.scheduleId
                _shareId.value = response.shareId?:""
                _meetDate.value = response.meetDate
                Log.d("ScheduleViewModel", "${shareId},${meetDate}")
                Log.d("ScheduleViewModel", scheduleId.value.toString())
            }.onFailure { error ->
                Log.e("ScheduleViewModel", "HTTP 요청 실패: $error")
            }
        }
    }
}
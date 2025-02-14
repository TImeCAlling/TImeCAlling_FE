package com.umc.timeCAlling.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import com.umc.timeCAlling.presentation.calendar.wakeup.WakeupPeopleRVA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun getScheduleByDate(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            scheduleRepository.getScheduleByDate(date).onSuccess { response ->
                if (response.schedules.isNotEmpty()) {
                    _schedules.value = response.schedules.sortedBy { it.meetTime }
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

<<<<<<< HEAD
    fun getScheduleUsers(scheduleId: Int) {
        viewModelScope.launch {
            scheduleRepository.getScheduleUsers(scheduleId).onSuccess { response ->
                Log.d("ScheduleViewModel", response.toString())
                _scheduleUsers.value = response
=======
    private val _detailSchedule = MutableLiveData<DetailScheduleResponseModel>()
    val detailSchedule: LiveData<DetailScheduleResponseModel> get() = _detailSchedule

    fun getDetailSchedule(checklistId: Int) {
        viewModelScope.launch {
            scheduleRepository.getDetailSchedule(checklistId).onSuccess { response ->
                _detailSchedule.value = response
>>>>>>> origin/FEAT/#62-홈화면-날짜별일정-api-ui-반영
            }.onFailure { error ->
                Log.e("ScheduleViewModel", "HTTP 요청 실패: $error")
            }
        }
    }
}
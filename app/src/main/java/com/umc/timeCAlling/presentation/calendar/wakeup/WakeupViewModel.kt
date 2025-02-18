package com.umc.timeCAlling.presentation.calendar.wakeup

import android.util.Log
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.WakeUpAlarmResponseDto
import com.umc.timeCAlling.data.service.AlarmService
import com.umc.timeCAlling.domain.model.request.CategoriesRequestModel
import com.umc.timeCAlling.domain.model.request.schedule.ScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmRequestModel
import com.umc.timeCAlling.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WakeupViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {
    private val _receiverId = MutableLiveData<Int>()
    val receiverId: LiveData<Int> get() = _receiverId
    fun setReceiverId(receiverId: Int) {
        _receiverId.value = receiverId
        Log.d("WakeupViewModel", "Receiver ID set to: $receiverId")
    }

    private val _sharedId = MutableLiveData<String>()
    val sharedId: LiveData<String> get() = _sharedId
    fun setSharedId(sharedId: String) {
        _sharedId.value = sharedId
        Log.d("WakeupViewModel", "Shared ID set to: $sharedId")
    }

    private val _scheduledDate = MutableLiveData<String>()
    val scheduledDate: LiveData<String> get() = _scheduledDate
    fun setScheduledDate(scheduledDate: String) {
        _scheduledDate.value = scheduledDate
        Log.d("WakeupViewModel", "Scheduled Date set to: $scheduledDate")
    }

    fun wakeUpAlarm() {
        val request = WakeUpAlarmRequestModel(
            receiverId = receiverId.value?:0,
            shareId = sharedId.value?:"",
            scheduledDate = scheduledDate.value?:""
        )
        viewModelScope.launch {
            alarmRepository.wakeUpAlarm(request).onSuccess { response ->
                Log.d("AlarmViewModel", "Wake up alarm success: ${response}")
            }.onFailure { error ->
                Log.e("AlarmViewModel", "Wake up alarm error: ${error}")
            }
        }
    }
}
package com.umc.timeCAlling.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _schedules = MutableLiveData<List<ScheduleByDateResponseModel>>()
    val schedules: LiveData<List<ScheduleByDateResponseModel>> get() = _schedules

    fun getScheduleByDate(date: String) {
        viewModelScope.launch {
            scheduleRepository.getScheduleByDate(date).onSuccess { response ->
                _schedules.value = response.schedules
                Log.d("test", response.toString())
            }.onFailure { error ->
                Log.d("test", error.toString())
            }
        }
    }
}
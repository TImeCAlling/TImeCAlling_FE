package com.umc.timeCAlling.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingTimeViewModel @Inject constructor() : ViewModel() {

    private val _timeList = MutableLiveData<List<String>>()
    val timeList: LiveData<List<String>> get() = _timeList

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> get() = _selectedTime

    init {
        // 초기 데이터 설정
        _timeList.value = listOf("15분", "30분", "45분", "1시간", "90분")
    }

    fun onTimeSelected(time: String) {
        // 선택된 시간 업데이트
        _selectedTime.value = time
    }
}
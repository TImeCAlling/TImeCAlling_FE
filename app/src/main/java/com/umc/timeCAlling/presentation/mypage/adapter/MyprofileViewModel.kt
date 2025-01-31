package com.umc.timeCAlling.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel
import com.umc.timeCAlling.domain.repository.MypageRepository
import com.umc.timeCAlling.util.network.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyprofileViewModel @Inject constructor(
    private val mypageRepository: MypageRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UiState<GetUserResponseModel>>(UiState.Loading)
    val userInfo: StateFlow<UiState<GetUserResponseModel>> = _userInfo.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _userInfo.value = UiState.Loading // 상태 초기화
            mypageRepository.getUser()
                .onSuccess { user ->
                    Log.d("MyprofileViewModel", "getUser() 성공: $user")
                    _userInfo.value = UiState.Success(user)
                }
                .onFailure { exception ->
                    Log.e("MyprofileViewModel", "getUser() 실패: ${exception.message}\n${exception.stackTraceToString()}")
                    _userInfo.value = UiState.Error(exception)
                }
        }
    }
}


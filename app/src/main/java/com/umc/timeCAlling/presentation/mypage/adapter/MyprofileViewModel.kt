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

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

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

    fun deleteUser() {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading // 삭제 요청 중
            mypageRepository.deleteUser()
                .onSuccess {
                    Log.d("MyprofileViewModel", "deleteUser() 성공")
                    _deleteState.value = UiState.Success(true)
                }
                .onFailure { exception ->
                    Log.e("MyprofileViewModel", "deleteUser() 실패: ${exception.message}\n${exception.stackTraceToString()}")
                    _deleteState.value = UiState.Error(exception)
                }
        }
    }
}


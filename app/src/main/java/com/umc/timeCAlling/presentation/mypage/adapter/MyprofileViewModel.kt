package com.umc.timeCAlling.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.request.mypage.UpdateUserRequestModel
import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel
import com.umc.timeCAlling.domain.repository.MypageRepository
import com.umc.timeCAlling.util.network.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyprofileViewModel @Inject constructor(
    private val mypageRepository: MypageRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UiState<GetUserResponseModel>>(UiState.Loading)
    val userInfo: StateFlow<UiState<GetUserResponseModel>> = _userInfo.asStateFlow()

    private var currentProfileImageUrl: String? = null

    private val _updateState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val updateState: StateFlow<UiState<Boolean>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteState: StateFlow<UiState<Boolean>> = _deleteState.asStateFlow()

    private val _logoutState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val logoutState: StateFlow<UiState<Boolean>> = _logoutState.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _userInfo.value = UiState.Loading // 상태 초기화
            mypageRepository.getUser()
                .onSuccess { user ->
                    Log.d("MyprofileViewModel", "getUser() 성공: $user")
                    _userInfo.value = UiState.Success(user)
                    currentProfileImageUrl = user.profileImage
                }
                .onFailure { exception ->
                    Log.e("MyprofileViewModel", "getUser() 실패: ${exception.message}\n${exception.stackTraceToString()}")
                    _userInfo.value = UiState.Error(exception)
                }
        }
    }

    fun updateUser(nickname: String?, avgPrepTime: Int?, freeTime: String?, imageFile: File?) {
        val (imagePart, updateUserRequestModel) = createMultipartRequest(nickname, avgPrepTime, freeTime, imageFile)

        viewModelScope.launch {
            _updateState.value = UiState.Loading
            mypageRepository.updateUser(imagePart, updateUserRequestModel)
                .onSuccess {
                    Log.d("MyprofileViewModel", "updateUser() 성공")
                    _updateState.value = UiState.Success(true)

                    currentProfileImageUrl = imageFile?.absolutePath ?: currentProfileImageUrl
                    // getUser() // 업데이트 후 최신 정보 다시 가져오기
                }
                .onFailure { exception ->
                    Log.e("MyprofileViewModel", "updateUser() 실패: ${exception.message}\n${exception.stackTraceToString()}")
                    _updateState.value = UiState.Error(exception)
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

    fun logoutUser() {
        viewModelScope.launch {
            _logoutState.value = UiState.Loading // 로그아웃 요청 중
            mypageRepository.logoutUser()
                .onSuccess {
                    Log.d("MyprofileViewModel", "logoutUser() 성공")
                    _logoutState.value = UiState.Success(true)
                }
                .onFailure { exception ->
                    Log.e("MyprofileViewModel", "logoutUser() 실패: ${exception.message}\n${exception.stackTraceToString()}")
                    _logoutState.value = UiState.Error(exception)
                }
        }
    }

    private fun createMultipartRequest(
        nickname: String?,
        avgPrepTime: Int?,
        freeTime: String?,
        imageFile: File?
    ): Pair<MultipartBody.Part?, RequestBody> {
        val userUpdateJson = JSONObject().apply {
            put("nickname", nickname ?: JSONObject.NULL) // ✅ 명시적 null 값 포함
            put("avgPrepTime", avgPrepTime ?: JSONObject.NULL) // ✅ 명시적 null 값 포함
            put("freeTime", freeTime ?: JSONObject.NULL) // ✅ 명시적 null 값 포함

            if (imageFile == null) {
                put("profileImage", currentProfileImageUrl ?: JSONObject.NULL)
            }
        }.toString()

        val userUpdateRequestBody = userUpdateJson.toRequestBody("application/json".toMediaTypeOrNull())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
        }

        return Pair(imagePart, userUpdateRequestBody)
    }


}


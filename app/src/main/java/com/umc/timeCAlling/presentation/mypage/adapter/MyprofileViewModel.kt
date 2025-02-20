package com.umc.timeCAlling.presentation.mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.R
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
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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

    fun updateUser(nickname: String?, avgPrepTime: Int?, freeTime: String?, imageFile: File?, context: Context) {
        val imagePart = try {
            val defaultProfileUrl = "https://timecalling-uploaded-files.s3.ap-northeast-2.amazonaws.com/profile_image.png"

            // 🔍 현재 `profileImage`가 기본 프로필 URL이면 drawable의 기본 이미지를 전송
            if (currentProfileImageUrl == defaultProfileUrl) {
                getMultipartBodyFromResource(context, R.drawable.ic_profile_default_default, "profileImage")
            } else if (imageFile != null) {
                getMultipartBodyFromFile(imageFile, "profileImage")
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e("Error creating MultipartBody.Part for profile image: ${e.message}")
            null
        }

        val userUpdateJson = JSONObject().apply {
            put("nickname", nickname ?: JSONObject.NULL)
            put("avgPrepTime", avgPrepTime ?: JSONObject.NULL)
            put("freeTime", freeTime ?: JSONObject.NULL)
        }.toString()

        val userUpdateRequestBody = userUpdateJson.toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch {
            _updateState.value = UiState.Loading
            mypageRepository.updateUser(imagePart, userUpdateRequestBody)
                .onSuccess {
                    Log.d("MyprofileViewModel", "updateUser() 성공")
                    _updateState.value = UiState.Success(true)

                    currentProfileImageUrl = imageFile?.absolutePath ?: currentProfileImageUrl
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
            put("nickname", nickname ?: JSONObject.NULL)
            put("avgPrepTime", avgPrepTime ?: JSONObject.NULL)
            put("freeTime", freeTime ?: JSONObject.NULL)
        }.toString()

        val userUpdateRequestBody = userUpdateJson.toRequestBody("application/json".toMediaTypeOrNull())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestFile)
        }

        return Pair(imagePart, userUpdateRequestBody)
    }

    private fun getMultipartBodyFromResource(context: Context, resourceId: Int, paramName: String): MultipartBody.Part {
        val file = File(context.cacheDir, "default_profile.png")

        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }

    private fun getMultipartBodyFromFile(file: File, paramName: String): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
    }


}


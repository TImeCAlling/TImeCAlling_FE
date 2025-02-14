package com.umc.timeCAlling.presentation.login.adapter

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.vector.path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.request.login.TokenRefreshRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoUserInfoResponseModel
import com.umc.timeCAlling.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val spf: SharedPreferences,
    application: Application,
    private val loginRepository: LoginRepository
) : ViewModel() {


    private val _kakaoAccessToken = MutableLiveData<String>()
    val kakaoAccessToken: LiveData<String> get() = _kakaoAccessToken

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    private val _profileImage = MutableLiveData<Uri?>()
    val profileImage: LiveData<Uri?> get() = _profileImage

    private val _avgPrepTime = MutableLiveData<Int>()
    val avgPrepTime: LiveData<Int> get() = _avgPrepTime

    private val _freeTime = MutableLiveData<String>()
    val freeTime: LiveData<String> get() = _freeTime

    private val _signupResult = MutableLiveData<KakaoSignupResponseModel?>()
    val signupResult: LiveData<KakaoSignupResponseModel?> get() = _signupResult

    private val _kakaoUserInfo = MutableLiveData<KakaoUserInfoResponseModel?>()
    val kakaoUserInfo: LiveData<KakaoUserInfoResponseModel?> get() = _kakaoUserInfo

    fun setKakaoAccessToken(token: String) {
        _kakaoAccessToken.value = token
    }

    fun setNickname(name: String) {
        _nickname.value = name
    }

    fun setProfileImage(uri: Uri) {
        _profileImage.value = uri
    }

    fun setAvgPrepTime(time: Int) {
        _avgPrepTime.value = time
    }

    fun setFreeTime(time: String) {
        _freeTime.value = time
    }

    private val context: Context = application

    fun signup() {
        val profileImagePart = try {
            val profileImageUri = _profileImage.value
                ?: throw IllegalArgumentException("Profile image URI is required.")
            val inputStream = context.contentResolver.openInputStream(profileImageUri)
            val selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
            val defaultImageStream = ByteArrayOutputStream().apply {
                selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
            }
            val defaultImageBytes = defaultImageStream.toByteArray()

            val requestBody =
                defaultImageBytes.toRequestBody("image/png".toMediaTypeOrNull()) // MIME 타입 구체화
            MultipartBody.Part.createFormData(
                "profileImage", "profile_image.png", requestBody
            )
        } catch (e: Exception) {
            Timber.e("Error creating MultipartBody.Part for profile image: ${e.message}")
            val emptyImageRequestBody =
                ByteArray(0).toRequestBody("image/png".toMediaTypeOrNull()) // 빈 이미지 요청
            MultipartBody.Part.createFormData(
                "profileImage", "profile_image.png", emptyImageRequestBody
            )
        }

        val request = KakaoSignupRequestModel(
            kakaoAccessToken = _kakaoAccessToken.value ?: "",
            nickname = _nickname.value ?: "",
            avgPrepTime = _avgPrepTime.value ?: 0,
            freeTime = _freeTime.value ?: ""
        )

        Log.d("SignupViewModel", "$request")

        viewModelScope.launch {
            loginRepository.kakaoSignup(profileImagePart, request).onSuccess { response ->
                Log.d("SignupViewModel", "API Response: $response")
                _signupResult.postValue(response) // 성공 시 데이터를 LiveData에 전달
            }.onFailure { error ->
                _signupResult.postValue(null)
                Log.d("SignupViewModel", "API Error: $error")
            }
        }
    }

    fun getKakaoUserInfo() {
        viewModelScope.launch {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("SignupViewModel", "카카오 유저 정보 가져오기 실패: ${error.message}")
                    _kakaoUserInfo.postValue(null)
                } else if (user != null) {
                    val userInfo = KakaoUserInfoResponseModel(
                        id = user.id ?: 0L,
                        nickname = user.kakaoAccount?.profile?.nickname ?: "이름 없음"
                    )
                    _kakaoUserInfo.postValue(userInfo)
                    Log.d("SignupViewModel", "카카오 유저 정보 가져오기 성공: ${userInfo.nickname}")
                }
            }
        }
    }

    fun handleLoginSuccess(accessToken: String, refreshToken: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            loginRepository.kakaoLogin(KakaoLoginRequestModel(accessToken)).onSuccess { response ->
                
                Log.d("SignupViewModel", "카카오 로그인 성공: Access Token = ${response.accessToken}")
                Log.d("SignupViewModel", "카카오 로그인 성공: Refresh Token = ${response.refreshToken}")

                spf.edit().apply {
                    putString("jwt", response.accessToken)  // 새 AccessToken 저장
                    putString("refreshToken", response.refreshToken)  // 새 RefreshToken 저장
                    putBoolean("isLoggedIn", true)
                    apply()
                }

                callback(true)
            }.onFailure { error ->
                Log.e("SignupViewModel", "서버 로그인 실패: ${error.message}")
                callback(false)
            }
        }
    }

    fun clearAuthToken() {
        spf.edit().apply {
            remove("jwt") // 🔥 accessToken 삭제
            remove("refreshToken") // 🔥 refreshToken 삭제
            remove("isLoggedIn") // 🔥 로그인 상태 초기화
            apply()
        }
        Log.d("SignupViewModel", "토큰 삭제 완료: accessToken 및 refreshToken 초기화됨")
    }

}
package com.umc.timeCAlling.presentation.signup.adapter

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import android.content.Context

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val spf: SharedPreferences,
    private val repository: LoginRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _kakaoUserId = MutableLiveData<String>()
    val kakaoUserId: LiveData<String> get() = _kakaoUserId

    private val _loginResult = MutableLiveData<KakaoLoginResponseModel?>()
    val loginResult: LiveData<KakaoLoginResponseModel?> get() = _loginResult

    private val context: Context = application

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> get() = _accessToken

    // accessToken 설정
    fun setAccessToken(token: String) {
        _accessToken.value = token
    }

    // 카카오 사용자 ID 설정
    fun setKakaoUserId(userId: String) {
        _kakaoUserId.value = userId
    }

    // 카카오 웹뷰 로그인 처리
    fun loginWithKakaoAccount() {
        val token = _accessToken.value
        if (token != null) {
            handleLoginSuccess(token)
        } else {
            Timber.e("AccessToken이 설정되지 않았습니다.")
            _loginResult.postValue(null) // 실패 처리
        }
    }

    private fun handleLoginSuccess(kakaoAccessToken: String) {
        viewModelScope.launch {
            try {
                // 로그인 요청
                val requestModel = KakaoLoginRequestModel(kakaoAccessToken)
                repository.kakaoLogin(requestModel).onSuccess { response ->
                    Timber.d("카카오 로그인 성공: ${response.accessToken}")

                    // SharedPreferences에 로그인 정보 저장
                    spf.edit().apply {
                        putString("jwt", response.accessToken)
                        putString("refreshToken", response.refreshToken)
                        putBoolean("isLoggedIn", true)
                        // putString("nickName", response.nickName)
                        apply()
                    }

                    _loginResult.value = response
                }.onFailure { error ->
                    Timber.e("카카오 로그인 실패: ${error.message}")
                    _loginResult.value = null // 실패 처리
                }
            } catch (e: Exception) {
                Timber.e("카카오 로그인 중 예외 발생: ${e.message}")
                _loginResult.value = null // 실패 처리
            }
        }
    }

    /*
    회원가입 부분
    */
}


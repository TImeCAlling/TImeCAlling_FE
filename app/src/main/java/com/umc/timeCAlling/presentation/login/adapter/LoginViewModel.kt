package com.umc.timeCAlling.presentation.login.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val spf: SharedPreferences,
    private val repository: LoginRepository,
    application: android.app.Application
) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<KakaoLoginResponseModel?>()
    val loginResult: LiveData<KakaoLoginResponseModel?> get() = _loginResult

    fun loginWithKakao(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            loginWithKakaoTalk(context)
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private fun loginWithKakaoTalk(context: Context) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.e("LoginViewModel", "카카오톡 로그인 실패: ${error.message}")
                _loginResult.postValue(null)
            } else if (token != null) {
                Log.d("LoginViewModel", "카카오톡 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun loginWithKakaoAccount(context: Context) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                Log.e("LoginViewModel", "카카오 계정 로그인 실패: ${error.message}")
                _loginResult.postValue(null)
            } else if (token != null) {
                Log.d("LoginViewModel", "카카오 계정 로그인 성공: ${token.accessToken}")
                handleLoginSuccess(token.accessToken)
            }
        }
    }

    private fun handleLoginSuccess(accessToken: String) {
        viewModelScope.launch {
            try {
                val requestModel = KakaoLoginRequestModel(accessToken)
                repository.kakaoLogin(requestModel).onSuccess { response ->
                    Log.d("LoginViewModel", "카카오 로그인 성공: ${response.accessToken}")

                    spf.edit().apply {
                        putString("jwt", response.accessToken)
                        putString("refreshToken", response.refreshToken)
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    _loginResult.value = response
                }.onFailure { error ->
                    Log.e("LoginViewModel", "서버 로그인 실패: ${error.message}")
                    _loginResult.value = null
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "서버 요청 중 예외 발생: ${e.message}")
                _loginResult.value = null
            }
        }
    }
}

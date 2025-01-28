package com.umc.timeCAlling.presentation.login.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel
import com.umc.timeCAlling.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    // LiveData for storing user input from each fragment
    private val _kakaoAccessToken = MutableLiveData<String>()
    val kakaoAccessToken: LiveData<String> get() = _kakaoAccessToken

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    private val _avgPrepTime = MutableLiveData<Int>()
    val avgPrepTime: LiveData<Int> get() = _avgPrepTime

    private val _freeTime = MutableLiveData<String>()
    val freeTime: LiveData<String> get() = _freeTime

    // LiveData to observe API response
    private val _signupResult = MutableLiveData<KakaoSignupResponseModel?>()
    val signupResult: LiveData<KakaoSignupResponseModel?> get() = _signupResult

    // Functions to set data from each fragment
    fun setKakaoAccessToken(token: String) {
        _kakaoAccessToken.value = token
    }

    fun setNickname(name: String) {
        _nickname.value = name
    }

    fun setAvgPrepTime(time: Int) {
        _avgPrepTime.value = time
    }

    fun setFreeTime(time: String) {
        _freeTime.value = time
    }

    // Function to call Signup API
    fun signup() {
        val token = _kakaoAccessToken.value
        val name = _nickname.value
        val prepTime = _avgPrepTime.value
        val free = _freeTime.value

        if (token != null && name != null && prepTime != null && free != null) {
            val request = KakaoSignupRequestModel(
                kakaoAccessToken = token,
                nickname = name,
                avgPrepTime = prepTime,
                freeTime = free
            )

            viewModelScope.launch {
                try {
                    val result = loginRepository.kakaoSignup(request)
                    result.onSuccess { response ->
                        _signupResult.postValue(response) // 성공 시 데이터를 LiveData에 전달
                    }.onFailure { error ->
                        _signupResult.postValue(null) // 실패 시 null 처리
                        // 추가로 에러 로그 출력 가능
                        println("Signup API Error: ${error.message}")
                    }
                } catch (e: Exception) {
                    _signupResult.postValue(null) // 예외 발생 시 null 처리
                    println("Signup API Exception: ${e.message}")
                }
            }
        } else {
            _signupResult.postValue(null) // Invalid input
        }
    }
}

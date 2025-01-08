package com.umc.timeCAlling.presentation.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    // private val onboardingRepository: OnboardingRepository
) : ViewModel(){

    /*
    private val _loginResult = MutableLiveData<NetworkResult<LoginModel>>()
    val loginResult : LiveData<NetworkResult<LoginModel>> = _loginResult

    private val _checkJwtResult = MutableLiveData<NetworkResult<JwtModel>>()
    val checkJwtResult : LiveData<NetworkResult<JwtModel>> = _checkJwtResult

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = loginRepository.login(request)
        }
    }

    fun checkJwt() {
        viewModelScope.launch {
            _checkJwtResult.value = loginRepository.checkJwt()
        }
    }
    */
}

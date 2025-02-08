package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.request.login.KakaoSignupRequestDto
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.dto.response.login.KakaoSignupResponseDto
import com.umc.timeCAlling.data.dto.response.login.TokenRefreshResponseDto
import okhttp3.MultipartBody

interface LoginDataSource {
    suspend fun kakaoLogin(requestDto:KakaoLoginRequestDto): BaseResponse<KakaoLoginResponseDto>
    suspend fun kakaoSignup(profileImage:MultipartBody.Part, requestDto:KakaoSignupRequestDto): BaseResponse<KakaoSignupResponseDto>
    suspend fun tokenRefresh(requestDto:TokenRefreshRequestDto): BaseResponse<TokenRefreshResponseDto>

}
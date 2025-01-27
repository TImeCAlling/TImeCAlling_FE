package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.dto.response.login.KakaoSignupResponseDto

interface LoginDataSource {
    suspend fun kakaoLogin(requestDto:KakaoLoginRequestDto): BaseResponse<KakaoLoginResponseDto>
    suspend fun kakaoSignup(): BaseResponse<KakaoSignupResponseDto>

}
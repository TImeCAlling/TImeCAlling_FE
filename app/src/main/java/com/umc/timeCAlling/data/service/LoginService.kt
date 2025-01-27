package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.dto.response.login.KakaoSignupResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/users/kakao/login")
    suspend fun kakaoLogin(
        @Body request: KakaoLoginRequestDto
    ): BaseResponse<KakaoLoginResponseDto>

    @POST("/api/users/kakao/signup")
    suspend fun kakaoSignup(): BaseResponse<KakaoSignupResponseDto>
}
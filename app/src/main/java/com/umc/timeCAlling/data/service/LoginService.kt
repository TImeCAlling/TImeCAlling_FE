package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.request.login.KakaoSignupRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.dto.response.login.KakaoSignupResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part

interface LoginService {
    @POST("/api/users/kakao/login")
    suspend fun kakaoLogin(
        @Body request: KakaoLoginRequestDto
    ): BaseResponse<KakaoLoginResponseDto>

    @POST("/api/users/kakao/signup")
    suspend fun kakaoSignup(
        @Body profileImage: String,
        @Body request: KakaoSignupRequestDto
    ): BaseResponse<KakaoSignupResponseDto>
}
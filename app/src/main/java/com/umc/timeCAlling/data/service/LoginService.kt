package com.umc.timeCAlling.data.service


import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLogintRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    // api 명세서의 api 주소 기입
    @POST("/api/users/kakao/login")
    suspend fun kakaoLogin(
        @Body request: KakaoLogintRequestDto
    ): BaseResponse<KakaoLoginResponseDto>
}
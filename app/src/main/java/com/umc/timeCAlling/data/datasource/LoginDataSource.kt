package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLogintRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto

interface LoginDataSource {
    suspend fun kakaoLogin(requestDto:KakaoLogintRequestDto): BaseResponse<KakaoLoginResponseDto>
}

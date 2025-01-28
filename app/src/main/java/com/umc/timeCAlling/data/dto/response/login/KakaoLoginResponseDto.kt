package com.umc.timeCAlling.data.dto.response.login

import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel

data class KakaoLoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
){
    fun toKakaoLoginResponseModel() =
        KakaoLoginResponseModel(userId, accessToken, refreshToken)
}
package com.umc.timeCAlling.data.dto.response.login

import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel

data class KakaoSignupResponseDto(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String
){
    fun toKakaoSignupResponseModel() =
        KakaoSignupResponseModel(userId, accessToken, refreshToken)
}
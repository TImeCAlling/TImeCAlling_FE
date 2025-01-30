package com.umc.timeCAlling.domain.model.response.login

data class KakaoLoginResponseModel(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String
)


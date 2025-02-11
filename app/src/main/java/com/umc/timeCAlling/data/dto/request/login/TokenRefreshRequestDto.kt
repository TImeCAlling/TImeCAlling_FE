package com.umc.timeCAlling.data.dto.request.login

data class TokenRefreshRequestDto(
    val accessToken: String,
    val refreshToken: String
)
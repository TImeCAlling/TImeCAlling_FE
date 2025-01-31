package com.umc.timeCAlling.domain.model.request.login

import com.umc.timeCAlling.data.dto.request.login.KakaoSignupRequestDto
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import java.io.Serializable

data class TokenRefreshRequestModel(
    val accessToken: String,
    val refreshToken: String
): Serializable {
    fun toTokenRefreshRequestDto() =
        TokenRefreshRequestDto(accessToken, refreshToken)
}
package com.umc.timeCAlling.data.dto.response.login

import com.umc.timeCAlling.domain.model.response.login.TokenRefreshResponseModel

data class TokenRefreshResponseDto(
    val userId: Int,
    val accessToken: String
){
    fun toTokenRefreshResponseModel() =
        TokenRefreshResponseModel(userId, accessToken)
}
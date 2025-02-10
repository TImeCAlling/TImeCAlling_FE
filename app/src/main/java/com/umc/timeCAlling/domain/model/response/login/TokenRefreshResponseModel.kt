package com.umc.timeCAlling.domain.model.response.login

data class TokenRefreshResponseModel(
    val userId: Int,
    val accessToken: String
)
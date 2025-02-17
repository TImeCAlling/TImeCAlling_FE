package com.umc.timeCAlling.data.dto.response.mypage

import com.umc.timeCAlling.domain.model.response.mypage.LogoutUserResponseModel


data class LogoutUserResponseDto(
    val userId: Int
) {
    fun toLogoutUserResponseModel() =
        LogoutUserResponseModel(userId)
}

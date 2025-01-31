package com.umc.timeCAlling.data.dto.response.mypage

import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel

data class GetUserResponseDto(
    val userId: Int,
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String,
    val success: Int,
    val failed: Int,
    val profileImage: String
) {
    fun toGetUserResponseModel() =
        GetUserResponseModel(userId, nickname, avgPrepTime, freeTime, success, failed, profileImage)
}

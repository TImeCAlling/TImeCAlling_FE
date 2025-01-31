package com.umc.timeCAlling.domain.model.response.mypage

data class GetUserResponseModel(
    val userId: Int,
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String,
    val success: Int,
    val failed: Int,
    val profileImage: String
)

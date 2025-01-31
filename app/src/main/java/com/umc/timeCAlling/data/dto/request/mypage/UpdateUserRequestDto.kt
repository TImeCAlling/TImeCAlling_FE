package com.umc.timeCAlling.data.dto.request.mypage

data class UpdateUserRequestDto(
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String
)

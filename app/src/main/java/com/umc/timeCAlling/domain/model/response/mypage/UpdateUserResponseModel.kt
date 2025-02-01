package com.umc.timeCAlling.domain.model.response.mypage

data class UpdateUserResponseModel(
    val freeTime: String,
    val avgPrepTime: Int,
    val nickname: String
)

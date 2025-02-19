package com.umc.timeCAlling.domain.model.response.alarm

data class WakeUpAlarmResponseModel(
    val receiverId: Int,
    val shareId: String,
    val status: String
)
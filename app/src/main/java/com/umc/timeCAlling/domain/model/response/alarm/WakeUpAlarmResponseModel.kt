package com.umc.timeCAlling.domain.model.response.alarm

data class WakeUpAlarmResponseModel(
    val receiverId: Int,
    val senderId: String,
    val scheduledDate: String
)
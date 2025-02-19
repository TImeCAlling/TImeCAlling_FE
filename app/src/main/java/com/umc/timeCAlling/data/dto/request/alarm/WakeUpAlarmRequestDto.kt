package com.umc.timeCAlling.data.dto.request.alarm

data class WakeUpAlarmRequestDto(
    val receiverId: Int,
    val shareId: String,
    val scheduledDate: String
)
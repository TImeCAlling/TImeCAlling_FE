package com.umc.timeCAlling.data.dto.request.alarm

data class WakeUpAlarmRequestDto(
    val receiverId: Int,
    val sharedId: String,
    val scheduledDate: String
)
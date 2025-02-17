package com.umc.timeCAlling.data.dto.request.alarm

data class WakeUpAlarmRequestDto(
    val receiverId: Int,
    val senderId: String,
    val scheduledDate: String
)
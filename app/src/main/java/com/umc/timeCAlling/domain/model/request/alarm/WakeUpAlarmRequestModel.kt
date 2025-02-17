package com.umc.timeCAlling.domain.model.response.alarm

import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto

data class WakeUpAlarmRequestModel(
    val receiverId: Int,
    val senderId: String,
    val scheduledDate: String
){
    fun toWakeUpAlarmRequestDto() = WakeUpAlarmRequestDto(receiverId, senderId, scheduledDate)
}
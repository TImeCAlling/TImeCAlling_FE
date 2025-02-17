package com.umc.timeCAlling.data.dto.response.alarm

import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmResponseModel

data class WakeUpAlarmResponseDto(
    val receiverId: Int,
    val senderId: String,
    val scheduledDate: String
){
    fun toWakeUpAlarmResponseModel() = WakeUpAlarmResponseModel(receiverId, senderId, scheduledDate)
}
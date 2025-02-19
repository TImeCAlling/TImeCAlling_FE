package com.umc.timeCAlling.data.dto.response.alarm

import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmResponseModel

data class WakeUpAlarmResponseDto(
    val receiverId: Int,
    val shareId: String,
    val status: String
){
    fun toWakeUpAlarmResponseModel() = WakeUpAlarmResponseModel(receiverId, shareId, status)
}
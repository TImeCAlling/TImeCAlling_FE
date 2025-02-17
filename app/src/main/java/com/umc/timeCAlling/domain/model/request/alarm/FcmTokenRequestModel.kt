package com.umc.timeCAlling.domain.model.request.alarm

import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto

data class FcmTokenRequestModel(
    val fcmToken: String
){
    fun toFcmTokenRequestDto() = FcmTokenRequestDto(fcmToken)
}

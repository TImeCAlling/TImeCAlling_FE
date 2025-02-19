package com.umc.timeCAlling.data.dto.response.alarm

import com.umc.timeCAlling.domain.model.response.alarm.FcmTokenResponseModel

data class FcmTokenResponseDto(
    val userId: Int
){
    fun toFcmTokenResponseModel() = FcmTokenResponseModel(userId)
}
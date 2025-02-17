package com.umc.timeCAlling.domain.model

import com.umc.timeCAlling.data.dto.FcmTokenDto

data class FcmTokenModel(
    val fcmToken: String
){
    fun toFcmTokenDto() = FcmTokenDto(fcmToken)
}

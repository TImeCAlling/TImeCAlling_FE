package com.umc.timeCAlling.data.dto

import com.umc.timeCAlling.domain.model.FcmTokenModel

data class FcmTokenDto(
    val fcmToken: String
){
    fun toFcmTokenModel() = FcmTokenModel(fcmToken)
}

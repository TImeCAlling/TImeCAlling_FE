package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.dto.response.alarm.WakeUpAlarmResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AlarmService {

    @POST("/api/push-requests")
    suspend fun wakeUpAlarm(
        @Body request: WakeUpAlarmRequestDto
    ): BaseResponse<WakeUpAlarmResponseDto>

    @PATCH("/api/push-requests/fcm-token")
    suspend fun fcmToken(
        @Body request: FcmTokenRequestDto
    ): BaseResponse<FcmTokenResponseDto>
}
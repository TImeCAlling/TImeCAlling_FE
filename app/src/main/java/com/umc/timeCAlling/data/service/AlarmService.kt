package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.FcmTokenDto
import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.WakeUpAlarmResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AlarmService {

    @POST("/api/push-requests")
    suspend fun wakeUpAlarm(
        @Body request: WakeUpAlarmRequestDto
    ): BaseResponse<WakeUpAlarmResponseDto>

    @POST("/api/push-requests/fcm-token")
    suspend fun fcmToken(
        @Body request: FcmTokenDto
    ): BaseResponse<FcmTokenDto>
}
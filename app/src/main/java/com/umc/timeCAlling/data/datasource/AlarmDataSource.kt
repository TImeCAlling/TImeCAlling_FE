package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.dto.response.alarm.WakeUpAlarmResponseDto

interface AlarmDataSource{
    suspend fun wakeUpAlarm(request: WakeUpAlarmRequestDto): BaseResponse<WakeUpAlarmResponseDto>
    suspend fun fcmToekn(request: FcmTokenRequestDto): BaseResponse<FcmTokenResponseDto>
}
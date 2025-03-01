package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.AlarmDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.alarm.FcmTokenRequestDto
import com.umc.timeCAlling.data.dto.request.alarm.WakeUpAlarmRequestDto
import com.umc.timeCAlling.data.dto.response.alarm.FcmTokenResponseDto
import com.umc.timeCAlling.data.dto.response.alarm.WakeUpAlarmResponseDto
import com.umc.timeCAlling.data.service.AlarmService
import javax.inject.Inject

class AlarmDataSourceImpl @Inject constructor(
    private val alarmService: AlarmService
): AlarmDataSource {
    override suspend fun wakeUpAlarm(request: WakeUpAlarmRequestDto): BaseResponse<WakeUpAlarmResponseDto> = alarmService.wakeUpAlarm(request)
    override suspend fun fcmToekn(request: FcmTokenRequestDto): BaseResponse<FcmTokenResponseDto> = alarmService.fcmToken(request)
}
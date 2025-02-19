package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.alarm.FcmTokenRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.FcmTokenResponseModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmResponseModel

interface AlarmRepository {
    suspend fun wakeUpAlarm(requestModel: WakeUpAlarmRequestModel): Result<WakeUpAlarmResponseModel>
    suspend fun fcmToken(requestModel: FcmTokenRequestModel): Result<FcmTokenResponseModel>
}
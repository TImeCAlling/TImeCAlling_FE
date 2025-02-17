package com.umc.timeCAlling.domain.repository

interface AlarmRepository {
    suspend fun wakeUpAlarm(requestModel: WakeUpAlarmRequestModel): Result<WakeUpAlarmResponseModel>
}
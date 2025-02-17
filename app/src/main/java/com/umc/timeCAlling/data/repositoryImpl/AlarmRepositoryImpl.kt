package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.AlarmDataSource
import com.umc.timeCAlling.domain.model.FcmTokenModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmResponseModel
import com.umc.timeCAlling.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDataSource: AlarmDataSource
) : AlarmRepository {
    override suspend fun wakeUpAlarm(requestModel: WakeUpAlarmRequestModel): Result<WakeUpAlarmResponseModel> =
        runCatching { alarmDataSource.wakeUpAlarm(requestModel.toWakeUpAlarmRequestDto()).result.toWakeUpAlarmResponseModel() }

    override suspend fun fcmToken(requestModel: FcmTokenModel): Result<FcmTokenModel> =
        runCatching { alarmDataSource.fcmToekn(requestModel.toFcmTokenDto()).result.toFcmTokenModel() }
}
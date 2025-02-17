package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.AlarmDataSource
import com.umc.timeCAlling.domain.model.request.alarm.FcmTokenRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.FcmTokenResponseModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmRequestModel
import com.umc.timeCAlling.domain.model.response.alarm.WakeUpAlarmResponseModel
import com.umc.timeCAlling.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmDataSource: AlarmDataSource
) : AlarmRepository {
    override suspend fun wakeUpAlarm(authorization: String, requestModel: WakeUpAlarmRequestModel): Result<WakeUpAlarmResponseModel> =
        runCatching { alarmDataSource.wakeUpAlarm(authorization, requestModel.toWakeUpAlarmRequestDto()).result.toWakeUpAlarmResponseModel() }

    override suspend fun fcmToken(authorization:String, requestModel: FcmTokenRequestModel): Result<FcmTokenResponseModel> =
        runCatching { alarmDataSource.fcmToekn(authorization, requestModel.toFcmTokenRequestDto()).result.toFcmTokenResponseModel() }
}
package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.domain.model.request.schedule.ScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun createSchedule(requestModel: ScheduleRequestModel): Result<CreateScheduleResponseModel> =
        runCatching { scheduleDataSource.createSchedule(requestModel.toScheduleRequestDto()).result.toCreateScheduleResponseModel() }

    override suspend fun editSchedule(requestModel: ScheduleRequestModel): Result<ScheduleResponseModel> =
        runCatching { scheduleDataSource.editSchedule(requestModel.toScheduleRequestDto()).result.toScheduleResponseModel() }

    override suspend fun deleteSchedule(scheduleId: Int): Result<ScheduleResponseModel> =
        runCatching { scheduleDataSource.deleteSchedule(scheduleId).result.toScheduleResponseModel() }
}
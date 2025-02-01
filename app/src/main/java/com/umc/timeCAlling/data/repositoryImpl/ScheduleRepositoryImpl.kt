package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.domain.model.request.schedule.CreateScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SchedulesResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodaySchedulesResponseModel
import com.umc.timeCAlling.domain.model.response.tmap.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.WalkTransportationModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import com.umc.timeCAlling.domain.repository.TmapRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun createSchedule(requestModel: CreateScheduleRequestModel): Result<CreateScheduleResponseModel> =
        runCatching { scheduleDataSource.createSchedule(requestModel.toCreateScheduleRequestDto()).result.toCreateScheduleResponseModel() }

    override suspend fun getScheduleByDate(date: String): Result<SchedulesResponseModel> =
        runCatching { scheduleDataSource.getScheduleByDate(date).result.toSchedulesResponseModel() }

    override suspend fun getTodaySchedules(): Result<TodaySchedulesResponseModel> =
        runCatching{ scheduleDataSource.getTodaySchedules().result.toTodaySchedulesResponseModel() }

    override suspend fun getSuccessRate(): Result<SuccessRateResponseModel> =
        runCatching{ scheduleDataSource.getSuccessRate().result.toSuccessRateResponseModel() }
}
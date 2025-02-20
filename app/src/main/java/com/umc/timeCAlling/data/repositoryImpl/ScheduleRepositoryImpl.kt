package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.domain.model.request.schedule.ScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.PastScheduleListResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleStatusResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SchedulesResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SharedScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodaySchedulesResponseModel
import com.umc.timeCAlling.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun createSchedule(requestModel: ScheduleRequestModel): Result<CreateScheduleResponseModel> =
        runCatching { scheduleDataSource.createSchedule(requestModel.toScheduleRequestDto()).result.toCreateScheduleResponseModel() }

    override suspend fun editSchedule(scheduleId: Int,requestModel: ScheduleRequestModel): Result<ScheduleResponseModel> =
        runCatching { scheduleDataSource.editSchedule(scheduleId,requestModel.toScheduleRequestDto()).result.toScheduleResponseModel() }

    override suspend fun deleteSchedule(scheduleId: Int): Result<ScheduleResponseModel> =
        runCatching { scheduleDataSource.deleteSchedule(scheduleId).result.toScheduleResponseModel() }
   
    override suspend fun getScheduleByDate(date: String): Result<SchedulesResponseModel> =
        runCatching { scheduleDataSource.getScheduleByDate(date).result.toSchedulesResponseModel() }

    override suspend fun getTodaySchedules(): Result<TodaySchedulesResponseModel> =
        runCatching{ scheduleDataSource.getTodaySchedules().result.toTodaySchedulesResponseModel() }

    override suspend fun getSuccessRate(): Result<SuccessRateResponseModel> =
        runCatching{ scheduleDataSource.getSuccessRate().result.toSuccessRateResponseModel() }

    override suspend fun getSharedSchedule(scheduleId: Int): Result<SharedScheduleResponseModel> =
        runCatching{ scheduleDataSource.getSharedSchedule(scheduleId).result.toSharedScheduleResponseModel() }

    override suspend fun postSharedSchedule(scheduleId: Int, requestModel: ScheduleRequestModel): Result<SchedulesResponseModel> =
        runCatching { scheduleDataSource.postSharedSchedule(scheduleId,requestModel.toScheduleRequestDto()).result.toSchedulesResponseModel() }

    override suspend fun getScheduleUsers(scheduleId: Int): Result<List<ScheduleUsersResponseModel>> =
        runCatching { scheduleDataSource.getScheduleUsers(scheduleId).result.map { it.toScheduleUsersResponseModel() } }

    override suspend fun getDetailSchedule(checklistId: Int): Result<DetailScheduleResponseModel> =
        runCatching { scheduleDataSource.getDetailSchedule(checklistId).result.toDetailScheduleResponseModel() }

    override suspend fun getScheduleStatus(scheduleId: Int): Result<ScheduleStatusResponseModel> =
        runCatching { scheduleDataSource.getScheduleStatus(scheduleId).result.toScheduleStatusResponseModel() }

    override suspend fun getPastCheckLists(): Result<PastScheduleListResponseModel> =
        runCatching { scheduleDataSource.getPastCheckLists().result.toPastScheduleListResponseModel() }
}
package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.schedule.ScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.PastScheduleListResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleStatusResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SchedulesResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SharedScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodaySchedulesResponseModel

interface ScheduleRepository {
    suspend fun createSchedule(requestModel: ScheduleRequestModel): Result<CreateScheduleResponseModel>
    suspend fun editSchedule(scheduleId: Int,requestModel: ScheduleRequestModel): Result<ScheduleResponseModel>
    suspend fun deleteSchedule(scheduleId:Int):Result<ScheduleResponseModel>
    suspend fun getScheduleByDate(date: String) : Result<SchedulesResponseModel>
    suspend fun getTodaySchedules() : Result<TodaySchedulesResponseModel>
    suspend fun getSuccessRate() : Result<SuccessRateResponseModel>
    suspend fun getSharedSchedule(scheduleId: Int) : Result<SharedScheduleResponseModel>
    suspend fun postSharedSchedule(scheduleId: Int,requestModel: ScheduleRequestModel) : Result<SchedulesResponseModel>
    suspend fun getScheduleUsers(scheduleId: Int) : Result<List<ScheduleUsersResponseModel>>
    suspend fun getDetailSchedule(checklistId: Int) : Result<DetailScheduleResponseModel>
    suspend fun getScheduleStatus(scheduleId: Int) : Result<ScheduleStatusResponseModel>
    suspend fun getPastCheckLists() : Result<PastScheduleListResponseModel>

}
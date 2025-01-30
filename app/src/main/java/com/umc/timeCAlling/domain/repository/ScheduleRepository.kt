package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.domain.model.request.schedule.CreateScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SchedulesResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel
import com.umc.timeCAlling.domain.model.response.schedule.TodaySchedulesResponseModel

interface ScheduleRepository {
    suspend fun createSchedule(requestModel: CreateScheduleRequestModel): Result<CreateScheduleResponseModel>
    suspend fun getScheduleByDate(date: String) : Result<SchedulesResponseModel>
    suspend fun getTodaySchedules() : Result<TodaySchedulesResponseModel>
    suspend fun getSuccessRate() : Result<SuccessRateResponseModel>
}
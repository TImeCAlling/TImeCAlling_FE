package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.domain.model.request.schedule.CreateScheduleRequestModel
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel

interface ScheduleRepository {
    suspend fun createSchedule(requestModel: CreateScheduleRequestModel): Result<CreateScheduleResponseModel>
}
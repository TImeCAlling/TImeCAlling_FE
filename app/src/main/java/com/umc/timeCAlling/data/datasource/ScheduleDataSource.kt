package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto

interface ScheduleDataSource {
    suspend fun createSchedule(requestDto:CreateScheduleRequestDto): BaseResponse<CreateScheduleResponseDto>
}
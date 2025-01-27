package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ScheduleService {

    @POST("/schedule-controller/schedules")
    suspend fun createSchedule(
            @Body request: CreateScheduleRequestDto
    ): BaseResponse<CreateScheduleResponseDto>
}
package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {

    @POST("/api/schedules")
    suspend fun createSchedule(
        @Body request: ScheduleRequestDto
    ): BaseResponse<CreateScheduleResponseDto>

    @PATCH("/api/schedules/{scheduleId}")
    suspend fun editSchedule(
        @Body request: ScheduleRequestDto
    ):BaseResponse<ScheduleResponseDto>

    @DELETE("/api/schedules/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Int
    ):BaseResponse<ScheduleResponseDto>
}
package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleByDateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleService {

    @POST("/schedule-controller/schedules")
    suspend fun createSchedule(
            @Body request: CreateScheduleRequestDto
    ): BaseResponse<CreateScheduleResponseDto>

    @GET("/api/schedules/date")
    suspend fun getScheduleByDate(
        @Query("date") date: String // ex. ?date=2025-01-20
    ) : BaseResponse<SchedulesResponseDto>

    @GET("/api/schedules/today")
    suspend fun getTodaySchedules(
    ) : BaseResponse<TodaySchedulesResponseDto>

    @GET("/api/schedules/success-rate")
    suspend fun getSuccessRate(
    ) : BaseResponse<SuccessRateResponseDto>
}
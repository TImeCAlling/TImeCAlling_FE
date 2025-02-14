package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.DetailScheduleResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleByDateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleUsersResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SharedScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleService {

    @POST("/api/schedules")
    suspend fun createSchedule(
        @Body request: ScheduleRequestDto
    ): BaseResponse<CreateScheduleResponseDto>

    @PATCH("/api/schedules/{scheduleId}")
    suspend fun editSchedule(
        @Path("scheduleId") scheduleId: Int,
        @Body request: ScheduleRequestDto
    ):BaseResponse<ScheduleResponseDto>

    @DELETE("/api/schedules/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Int
    ):BaseResponse<ScheduleResponseDto>

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

    @GET("/api/schedules/share/{scheduleId}")
    suspend fun getSharedSchedule(
        @Path("scheduleId") scheduleId: Int
    ) : BaseResponse<SharedScheduleResponseDto>

    @POST("/api/schedules/share/{scheduleId}")
    suspend fun postSharedSchedule(
        @Path("scheduleId") scheduleId: Int,
        @Body request: ScheduleRequestDto
    ): BaseResponse<SchedulesResponseDto>

    @GET("/api/schedules/{scheduleId}/users")
    suspend fun getScheduleUsers(
        @Path("scheduleId") scheduleId: Int
    ): BaseResponse<List<ScheduleUsersResponseDto>>

    @GET("/api/schedules/{checklistId}")
    suspend fun getDetailSchedule(
        @Path("checklistId") checklistId: Int
    ) : BaseResponse<DetailScheduleResponseDto>
}
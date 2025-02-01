package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto

interface ScheduleDataSource {
    suspend fun createSchedule(requestDto:ScheduleRequestDto): BaseResponse<CreateScheduleResponseDto>
    suspend fun editSchedule(scheduleId: Int,requestDto:ScheduleRequestDto):BaseResponse<ScheduleResponseDto>
    suspend fun deleteSchedule(scheduleId:Int):BaseResponse<ScheduleResponseDto>
    suspend fun getScheduleByDate(date: String): BaseResponse<SchedulesResponseDto>
    suspend fun getTodaySchedules(): BaseResponse<TodaySchedulesResponseDto>
    suspend fun getSuccessRate(): BaseResponse<SuccessRateResponseDto>
}
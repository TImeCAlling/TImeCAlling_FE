package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleByDateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto

interface ScheduleDataSource {
    suspend fun createSchedule(requestDto:CreateScheduleRequestDto): BaseResponse<CreateScheduleResponseDto>
    suspend fun getScheduleByDate(date: String): BaseResponse<SchedulesResponseDto>
    suspend fun getTodaySchedules(): BaseResponse<TodaySchedulesResponseDto>
    suspend fun getSuccessRate(): BaseResponse<SuccessRateResponseDto>
}
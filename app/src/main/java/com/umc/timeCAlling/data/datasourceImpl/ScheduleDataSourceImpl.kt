package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto
import com.umc.timeCAlling.data.service.ScheduleService
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val scheduleService: ScheduleService
): ScheduleDataSource {
    override suspend fun createSchedule(requestDto: CreateScheduleRequestDto): BaseResponse<CreateScheduleResponseDto> = scheduleService.createSchedule(requestDto)
    override suspend fun getScheduleByDate(date: String): BaseResponse<SchedulesResponseDto> = scheduleService.getScheduleByDate(date = date)
    override suspend fun getTodaySchedules(): BaseResponse<TodaySchedulesResponseDto> = scheduleService.getTodaySchedules()
    override suspend fun getSuccessRate(): BaseResponse<SuccessRateResponseDto> = scheduleService.getSuccessRate()
}
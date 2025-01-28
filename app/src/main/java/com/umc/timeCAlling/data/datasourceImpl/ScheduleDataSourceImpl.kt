package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto
import com.umc.timeCAlling.data.service.ScheduleService
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val scheduleService: ScheduleService
): ScheduleDataSource {
    override suspend fun createSchedule(requestDto: ScheduleRequestDto): BaseResponse<CreateScheduleResponseDto> = scheduleService.createSchedule(requestDto)
    override suspend fun editSchedule(requestDto: ScheduleRequestDto): BaseResponse<ScheduleResponseDto> = scheduleService.editSchedule(requestDto)
    override suspend fun deleteSchedule(scheduleId: Int): BaseResponse<ScheduleResponseDto> = scheduleService.deleteSchedule(scheduleId)
}
package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.ScheduleDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SharedScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto
import com.umc.timeCAlling.data.service.ScheduleService
import javax.inject.Inject

class ScheduleDataSourceImpl @Inject constructor(
    private val scheduleService: ScheduleService
): ScheduleDataSource {
    override suspend fun createSchedule(requestDto: ScheduleRequestDto): BaseResponse<CreateScheduleResponseDto> = scheduleService.createSchedule(requestDto)
    override suspend fun editSchedule(scheduleId: Int,requestDto: ScheduleRequestDto): BaseResponse<ScheduleResponseDto> = scheduleService.editSchedule(scheduleId,requestDto)
    override suspend fun deleteSchedule(scheduleId: Int): BaseResponse<ScheduleResponseDto> = scheduleService.deleteSchedule(scheduleId)
    override suspend fun getScheduleByDate(date: String): BaseResponse<SchedulesResponseDto> = scheduleService.getScheduleByDate(date = date)
    override suspend fun getTodaySchedules(): BaseResponse<TodaySchedulesResponseDto> = scheduleService.getTodaySchedules()
    override suspend fun getSuccessRate(): BaseResponse<SuccessRateResponseDto> = scheduleService.getSuccessRate()
    override suspend fun getSharedSchedule(scheduleId: Int): BaseResponse<SharedScheduleResponseDto> = scheduleService.getSharedSchedule(scheduleId)
    override suspend fun postSharedSchedule(scheduleId: Int, requestDto: ScheduleRequestDto): BaseResponse<SchedulesResponseDto> = scheduleService.postSharedSchedule(scheduleId,requestDto)
}
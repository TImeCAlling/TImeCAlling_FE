package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.request.schedule.ScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleUsersResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.DetailScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.PastScheduleListResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.ScheduleStatusResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SharedScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto
import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto

interface ScheduleDataSource {
    suspend fun createSchedule(requestDto:ScheduleRequestDto): BaseResponse<CreateScheduleResponseDto>
    suspend fun editSchedule(scheduleId: Int,requestDto:ScheduleRequestDto):BaseResponse<ScheduleResponseDto>
    suspend fun deleteSchedule(scheduleId:Int):BaseResponse<ScheduleResponseDto>
    suspend fun getScheduleByDate(date: String): BaseResponse<SchedulesResponseDto>
    suspend fun getTodaySchedules(): BaseResponse<TodaySchedulesResponseDto>
    suspend fun getSuccessRate(): BaseResponse<SuccessRateResponseDto>
    suspend fun getSharedSchedule(scheduleId: Int): BaseResponse<SharedScheduleResponseDto>
    suspend fun postSharedSchedule(scheduleId: Int,requestDto:ScheduleRequestDto): BaseResponse<SchedulesResponseDto>
    suspend fun getScheduleUsers(scheduleId: Int): BaseResponse<List<ScheduleUsersResponseDto>>
    suspend fun getDetailSchedule(checklistId: Int): BaseResponse<DetailScheduleResponseDto>
    suspend fun getScheduleStatus(scheduleId: Int): BaseResponse<ScheduleStatusResponseDto>
    suspend fun getPastCheckLists(): BaseResponse<PastScheduleListResponseDto>
}
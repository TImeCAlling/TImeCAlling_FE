package com.umc.timeCAlling.data.dto.request.schedule

import com.umc.timeCAlling.domain.model.response.schedule.ScheduleResponseModel

data class ScheduleResponseDto(
    val scheduleId : Int
){
    fun toScheduleResponseModel() = ScheduleResponseModel(scheduleId)
}
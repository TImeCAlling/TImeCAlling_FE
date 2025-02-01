package com.umc.timeCAlling.data.dto.request.schedule

import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel

data class CreateScheduleResponseDto(
    val scheduleId : Int,
    val shareId : String?,
    val createdAt : String
){
    fun toCreateScheduleResponseModel() = CreateScheduleResponseModel(scheduleId, shareId, createdAt)
}
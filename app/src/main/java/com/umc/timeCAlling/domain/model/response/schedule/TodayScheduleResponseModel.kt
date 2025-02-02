package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.TodayScheduleResponseDto

data class TodayScheduleResponseModel(
    val scheduleId : Int,
    val checkListId: Int,
    val name: String,
    val body: String,
    val meetTime: String
) {
    fun toTodayScheduleResponseDto() = TodayScheduleResponseDto(scheduleId, checkListId, name, body, meetTime)
}

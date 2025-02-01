package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.TodayScheduleResponseModel

data class TodayScheduleResponseDto(
    val scheduleId : Int,
    val checkListId: Int,
    val name: String,
    val body: String,
    val meetTime: String
) {
    fun toTodayScheduleResponseModel() = TodayScheduleResponseModel(scheduleId, checkListId, name, body, meetTime)
}

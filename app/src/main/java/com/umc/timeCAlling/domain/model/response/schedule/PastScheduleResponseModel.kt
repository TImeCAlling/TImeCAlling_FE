package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.PastScheduleResponseDto

data class PastScheduleResponseModel(
    val scheduleId: Int,
    val checkListId: Int,
    val name: String,
    val body: String,
    val meetTime: String,
    val date: String
) {
    fun toPastScheduleResponseDto() = PastScheduleResponseDto(scheduleId, checkListId, name, body, meetTime, date)
}

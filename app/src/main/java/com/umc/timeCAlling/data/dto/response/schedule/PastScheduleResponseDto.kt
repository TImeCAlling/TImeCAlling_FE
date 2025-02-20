package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.PastScheduleResponseModel

data class PastScheduleResponseDto(
    val scheduleId: Int,
    val checkListId: Int,
    val name: String,
    val body: String,
    val meetTime: String,
    val date: String
) {
    fun toPastScheduleResponseModel() = PastScheduleResponseModel(scheduleId, checkListId, name, body, meetTime, date)
}

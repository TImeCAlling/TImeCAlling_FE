package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.ScheduleStatusResponseDto

data class ScheduleStatusResponseModel(
    val name: String,
    val meetTime: String,
    val totalTime: String,
    val leftTime: Int
) {
    fun toScheduleStatusResponseDto() = ScheduleStatusResponseDto(name, meetTime, totalTime, leftTime)
}

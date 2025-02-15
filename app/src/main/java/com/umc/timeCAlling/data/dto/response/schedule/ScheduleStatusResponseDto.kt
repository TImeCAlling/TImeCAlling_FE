package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.ScheduleStatusResponseModel

data class ScheduleStatusResponseDto(
    val name: String,
    val meetTime: String,
    val totalTime: String,
    val leftTime: Int
) {
    fun toScheduleStatusResponseModel() = ScheduleStatusResponseModel(name, meetTime, totalTime, leftTime)
}

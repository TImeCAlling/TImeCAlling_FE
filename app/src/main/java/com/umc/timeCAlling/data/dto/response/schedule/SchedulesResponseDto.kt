package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.SchedulesResponseModel

data class SchedulesResponseDto(
    val schedules : List<ScheduleByDateResponseDto>
) {
    fun toSchedulesResponseModel() = SchedulesResponseModel(schedules.map{it.toScheduleByDateResponseModel()})
}

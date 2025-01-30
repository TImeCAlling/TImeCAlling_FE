package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.SchedulesResponseDto

data class SchedulesResponseModel(
    val schedules : List<ScheduleByDateResponseModel>
) {
    fun toSchedulesResponseDto() = SchedulesResponseDto(schedules.map{it.toScheduleByDateResponseDto()})
}

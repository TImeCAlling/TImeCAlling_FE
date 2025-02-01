package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.TodaySchedulesResponseModel

data class TodaySchedulesResponseDto(
    val schedules: List<TodayScheduleResponseDto>
) {
    fun toTodaySchedulesResponseModel() = TodaySchedulesResponseModel(schedules.map{it.toTodayScheduleResponseModel()})
}

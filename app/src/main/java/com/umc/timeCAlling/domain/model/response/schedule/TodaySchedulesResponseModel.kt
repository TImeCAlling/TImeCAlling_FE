package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.TodaySchedulesResponseDto

data class TodaySchedulesResponseModel(
    val schedules: List<TodayScheduleResponseModel>
) {
    fun toTodaySchedulesResponseDto() = TodaySchedulesResponseDto(schedules.map{it.toTodayScheduleResponseDto()})
}

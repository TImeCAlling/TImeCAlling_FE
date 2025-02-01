package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.ScheduleByDateResponseDto
import com.umc.timeCAlling.domain.model.response.CategoriesResponseModel

data class ScheduleByDateResponseModel(
    val categories: List<CategoriesResponseModel>,
    val checkListId: Int,
    val isWritten: Boolean,
    val isRepeat: Boolean,
    val meetTime: String,
    val name: String,
    val repeatDays: List<String>,
    val scheduleId: Int
) {
    fun toScheduleByDateResponseDto() = ScheduleByDateResponseDto(categories.map{it.toCategoriesResponseDto()}, checkListId, isWritten, isRepeat, meetTime, name, repeatDays, scheduleId)
}

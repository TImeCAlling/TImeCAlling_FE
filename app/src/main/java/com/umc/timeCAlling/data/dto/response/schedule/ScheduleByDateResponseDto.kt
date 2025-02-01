package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import com.umc.timeCAlling.domain.model.response.schedule.ScheduleByDateResponseModel

data class ScheduleByDateResponseDto (
    val categories: List<CategoriesResponseDto>,
    val checkListId: Int,
    val isWritten: Boolean,
    val isRepeat: Boolean,
    val meetTime: String,
    val name: String,
    val repeatDays: List<String>,
    val scheduleId: Int
) {
    fun toScheduleByDateResponseModel() = ScheduleByDateResponseModel(categories.map{it.toCategoriesResponseModel()}, checkListId, isWritten, isRepeat, meetTime, name, repeatDays, scheduleId)
}
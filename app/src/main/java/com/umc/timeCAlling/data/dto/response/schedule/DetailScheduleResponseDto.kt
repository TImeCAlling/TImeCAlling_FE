package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.DetailScheduleResponseModel

data class DetailScheduleResponseDto(
    val scheduleId: Int,
    val name: String,
    val meetDate: String,
    val meetTime: String,
    val place: String,
    val repeatDays: List<String>?,
    val moveTime: Int,
    val body: String,
    val freeTime: String,
    val isRepeat: Boolean,
    val start: String?,
    val end: String?,
    val categories: List<CategoriesByDetailResponseDto>,
    val shareId: String?
) {
    fun toDetailScheduleResponseModel() = DetailScheduleResponseModel(scheduleId, name, meetDate, meetTime, place, repeatDays, moveTime, body, freeTime, isRepeat, start, end, categories.map { it.toCategoriesByDetailResponseModel() }, shareId)
}

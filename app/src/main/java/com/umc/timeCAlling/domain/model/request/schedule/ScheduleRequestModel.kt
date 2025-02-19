package com.umc.timeCAlling.domain.model.request.schedule

import com.umc.timeCAlling.data.dto.request.schedule.ScheduleRequestDto
import com.umc.timeCAlling.domain.model.request.CategoriesRequestModel
import java.io.Serializable

data class ScheduleRequestModel(
    val name : String,
    val body : String?,
    val meetDate : String,
    val meetTime : String,
    val place : String,
    val longitude : String,
    val latitude : String,
    val moveTime : Int,
    val freeTime : String,
    val repeatDays : List<String>?,
    val isRepeat : Boolean,
    val start : String?,
    val end : String?,
    val categories : List<CategoriesRequestModel>
): Serializable {
    fun toScheduleRequestDto() =
        ScheduleRequestDto(name, body, meetDate,meetTime, place, longitude, latitude, moveTime, freeTime, repeatDays, isRepeat, start, end, categories.map{it.toCategoriesRequestDto()})
}

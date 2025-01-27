package com.umc.timeCAlling.domain.model.request.schedule

import com.umc.timeCAlling.data.dto.request.CategoriesRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleRequestDto
import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.domain.model.request.CategoriesRequestModel
import java.io.Serializable

data class CreateScheduleRequestModel(
    val name : String,
    val body : String,
    val meetTime : String,
    val place : String,
    val longitude : String,
    val latitude : String,
    val moveTime : Int,
    val freeTime : String,
    val repeatDays : List<String>,
    val isRepeat : Boolean,
    val start : String,
    val end : String,
    val categories : List<CategoriesRequestModel>
): Serializable {
    fun toCreateScheduleRequestDto() =
        CreateScheduleRequestDto(name, body, meetTime, place, longitude, latitude, moveTime, freeTime, repeatDays, isRepeat, start, end, categories.map{it.toCategoriesRequestDto()})
}

package com.umc.timeCAlling.data.dto.request.schedule

import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import com.umc.timeCAlling.domain.model.response.schedule.CreateScheduleResponseModel

data class CreateScheduleResponseDto(
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
    val categories : List<CategoriesResponseDto>
){
    fun toCreateScheduleResponseModel() = CreateScheduleResponseModel(name, body, meetTime, place, longitude, latitude, moveTime, freeTime, repeatDays, isRepeat, start, end, categories.map{it.toCategoriesResponseModel()})
}
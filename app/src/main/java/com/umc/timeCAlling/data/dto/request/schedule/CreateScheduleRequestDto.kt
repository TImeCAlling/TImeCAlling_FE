package com.umc.timeCAlling.data.dto.request.schedule

import com.umc.timeCAlling.data.dto.request.CategoriesRequestDto

data class CreateScheduleRequestDto(
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
    val categories : List<CategoriesRequestDto>
)
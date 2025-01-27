package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import com.umc.timeCAlling.domain.model.response.CategoriesResponseModel
import java.io.Serializable

data class CreateScheduleResponseModel(
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
    val categories : List<CategoriesResponseModel>
)
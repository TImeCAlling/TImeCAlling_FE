package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import com.umc.timeCAlling.domain.model.response.schedule.SharedScheduleResponseModel

data class SharedScheduleResponseDto(
    val nickname : String,
    val name : String,
    val meetDate : String,
    val meetTime : String,
    val place : String,
    val longitude : String,
    val latitude : String,
    val repeatDays : List<String>?,
    val isRepeat : Boolean,
    val start : String?,
    val end : String?
){
    fun toSharedScheduleResponseModel() = SharedScheduleResponseModel(nickname, name, meetDate, meetTime, place, longitude, latitude, repeatDays, isRepeat, start, end)
}

package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto

data class SharedScheduleResponseModel(
    val nickname : String,
    val name : String,
    val meetDate : String,
    val meetTime : String,
    val place : String,
    val longitude : String,
    val latitude : String,
    val repeatDays : List<String>,
    val isRepeat : Boolean,
    val start : String,
    val end : String
)

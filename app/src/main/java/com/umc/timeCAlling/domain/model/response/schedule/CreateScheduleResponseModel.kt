package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.request.schedule.CreateScheduleResponseDto
import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import com.umc.timeCAlling.domain.model.response.CategoriesResponseModel
import java.io.Serializable

data class CreateScheduleResponseModel(
    val scheduleId : Int,
    val shareId : String?,
    val createdAt : String
)
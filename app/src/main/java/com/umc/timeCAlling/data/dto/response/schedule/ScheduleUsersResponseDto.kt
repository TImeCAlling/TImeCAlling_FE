package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel

data class ScheduleUsersResponseDto(
    val id : Int,
    val nickname : String,
    val profile : String
){
    fun toScheduleUsersResponseModel() = ScheduleUsersResponseModel(id, nickname, profile)
}

package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.ScheduleUsersResponseModel

data class ScheduleUsersResponseDto(
    val userId : Int,
    val nickname : String,
    val profile : String
){
    fun toScheduleUsersResponseModel() = ScheduleUsersResponseModel(userId, nickname, profile)
}

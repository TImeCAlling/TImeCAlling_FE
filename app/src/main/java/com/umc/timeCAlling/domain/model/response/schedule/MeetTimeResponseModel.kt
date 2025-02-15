package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.MeetTimeResponseDto

data class MeetTimeResponseModel(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
) {
    fun toMeetTimeResponseDto() = MeetTimeResponseDto(hour, minute, second, nano)
}

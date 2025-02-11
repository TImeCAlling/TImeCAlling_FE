package com.umc.timeCAlling.data.dto.response

import com.umc.timeCAlling.domain.model.response.schedule.MeetTimeResponseModel

data class MeetTimeResponseDto(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
) {
    fun toMeetTimeResponseModel() = MeetTimeResponseModel(hour, minute, second, nano)
}

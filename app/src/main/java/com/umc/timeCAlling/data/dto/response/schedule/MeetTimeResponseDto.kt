package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.MeetTimeResponseModel

data class MeetTimeResponseDto(
    val hour: Int?,
    val minute: Int?,
    val nano: Int?,
    val second: Int?
) {
    fun toMeetTimeResponseModel() = MeetTimeResponseModel(hour, minute, nano, second)
}
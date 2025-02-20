package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.PastScheduleListResponseDto

data class PastScheduleListResponseModel(
    val checkLists : List<PastScheduleResponseModel>?
) {
    fun toPastScheduleListResponseDto() = PastScheduleListResponseDto(checkLists?.map { it.toPastScheduleResponseDto() })
}

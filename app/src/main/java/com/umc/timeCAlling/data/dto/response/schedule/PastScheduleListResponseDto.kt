package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.PastScheduleListResponseModel

data class PastScheduleListResponseDto(
    val checkLists : List<PastScheduleResponseDto>?
) {
    fun toPastScheduleListResponseModel() = PastScheduleListResponseModel(checkLists?.map { it.toPastScheduleResponseModel() })
}

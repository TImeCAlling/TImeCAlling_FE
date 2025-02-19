package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.SuccessRateResponseDto

data class SuccessRateResponseModel(
    val total: Int,
    val success: Int,
    val failed: Int,
    val successRate: Float,
) {
    fun toSuccessRateResponseDto() = SuccessRateResponseDto(total, success, failed, successRate)
}

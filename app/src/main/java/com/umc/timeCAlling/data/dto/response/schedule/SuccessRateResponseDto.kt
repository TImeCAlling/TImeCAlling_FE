package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.SuccessRateResponseModel

data class SuccessRateResponseDto(
    val total: Int,
    val success: Int,
    val failed: Int,
    val successRate: Float,
) {
    fun toSuccessRateResponseModel() = SuccessRateResponseModel(total, success, failed, successRate)
}

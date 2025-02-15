package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.CategoriesByDetailResponseDto

data class CategoriesByDetailResponseModel(
    val categoryName: String,
    val color: Int
) {
    fun toCategoriesByDetailResponseDto() = CategoriesByDetailResponseDto(categoryName, color)
}


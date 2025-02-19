package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.CategoriesByDetailResponseModel

data class CategoriesByDetailResponseDto(
    val categoryName: String,
    val color: Int
) {
    fun toCategoriesByDetailResponseModel() = CategoriesByDetailResponseModel(categoryName, color)

}
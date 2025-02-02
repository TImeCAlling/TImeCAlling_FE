package com.umc.timeCAlling.domain.model.response.schedule

import com.umc.timeCAlling.data.dto.response.schedule.CategoriesByDateResponseDto

data class CategoriesByDateResponseModel(
    val categoryName: String,
    val categoryColor: Int
) {
    fun toCategoriesByDateResponseDto() = CategoriesByDateResponseDto(categoryName, categoryColor)
}

package com.umc.timeCAlling.data.dto.response.schedule

import com.umc.timeCAlling.domain.model.response.schedule.CategoriesByDateResponseModel

data class CategoriesByDateResponseDto(
    val categoryName: String,
    val categoryColor: Int
) {
    fun toCategoriesByDateResponseModel() = CategoriesByDateResponseModel(categoryName, categoryColor)
}

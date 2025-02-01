package com.umc.timeCAlling.domain.model.request

import com.umc.timeCAlling.data.dto.request.CategoriesRequestDto
import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import java.io.Serializable

data class CategoriesRequestModel(
    val categoryName : String,
    val color : Int
): Serializable {
    fun toCategoriesRequestDto() =
        CategoriesRequestDto(categoryName,color)
}
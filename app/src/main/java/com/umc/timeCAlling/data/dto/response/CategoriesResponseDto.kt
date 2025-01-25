package com.umc.timeCAlling.data.dto.response

import com.umc.timeCAlling.domain.model.response.CategoriesResponseModel

data class CategoriesResponseDto(
    val category : String,
    val color : Int
){
    fun toCategoriesResponseModel() = CategoriesResponseModel(category, color)
}
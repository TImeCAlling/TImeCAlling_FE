package com.umc.timeCAlling.domain.model.response

import com.umc.timeCAlling.data.dto.response.CategoriesResponseDto
import java.io.Serializable

data class CategoriesResponseModel(
    val category : String,
    val color : Int
)
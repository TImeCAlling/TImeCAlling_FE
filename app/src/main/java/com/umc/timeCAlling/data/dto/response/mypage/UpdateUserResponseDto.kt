package com.umc.timeCAlling.data.dto.response.mypage

import com.umc.timeCAlling.domain.model.response.mypage.UpdateUserResponseModel

data class UpdateUserResponseDto(
    val freeTime: String,
    val avgPrepTime: Int,
    val nickname: String
) {
    fun toUpdateUserResponseModel() =
        UpdateUserResponseModel(freeTime, avgPrepTime, nickname)
}

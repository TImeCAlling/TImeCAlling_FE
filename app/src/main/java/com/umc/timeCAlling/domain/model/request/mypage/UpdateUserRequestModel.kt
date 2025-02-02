package com.umc.timeCAlling.domain.model.request.mypage

import com.umc.timeCAlling.data.dto.request.mypage.UpdateUserRequestDto
import java.io.Serializable

data class UpdateUserRequestModel(
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String
): Serializable {
    fun toUpdateUserRequestDto() =
        UpdateUserRequestDto(nickname, avgPrepTime, freeTime)
}

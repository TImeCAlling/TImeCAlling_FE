package com.umc.timeCAlling.data.dto.response.mypage

import com.umc.timeCAlling.domain.model.response.mypage.DeleteUserResponseModel

data class DeleteUserResponseDto(
    val userId: Int
) {
    fun toDeleteUserResponseModel() =
        DeleteUserResponseModel(userId)
}

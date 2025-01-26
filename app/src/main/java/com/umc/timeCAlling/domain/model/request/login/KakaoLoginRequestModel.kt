package com.umc.timeCAlling.domain.model.request.login

import com.umc.timeCAlling.data.dto.request.login.KakaoLogintRequestDto
import java.io.Serializable

data class KakaoLoginRequestModel(
    val kakaoAccessToken: String
): Serializable {
    fun toKakaoLoginRequestDto() =
        KakaoLogintRequestDto(kakaoAccessToken)
}
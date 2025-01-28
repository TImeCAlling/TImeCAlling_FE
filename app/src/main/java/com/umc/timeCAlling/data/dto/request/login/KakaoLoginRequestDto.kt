package com.umc.timeCAlling.data.dto.request.login

import java.io.Serializable

data class KakaoLoginRequestDto(
    val kakaoAccessToken: String
): Serializable {
    fun toKakaoLoginRequestDto() =
        KakaoLoginRequestDto(kakaoAccessToken)
}
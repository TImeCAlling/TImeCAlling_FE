package com.umc.timeCAlling.data.dto.request.login

import java.io.Serializable

data class KakaoLogintRequestDto(
    val kakaoAccessToken: String
): Serializable {
    fun toKakaoLogintRequestDto() =
        KakaoLogintRequestDto(kakaoAccessToken)
}

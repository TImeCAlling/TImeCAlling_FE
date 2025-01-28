package com.umc.timeCAlling.domain.model.request.login

import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.request.login.KakaoSignupRequestDto
import java.io.Serializable

data class KakaoSignupRequestModel(
    val kakaoAccessToken: String,
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String
): Serializable {
    fun toKakaoSignupRequestDto() =
        KakaoSignupRequestDto(kakaoAccessToken, nickname, avgPrepTime, freeTime)
}
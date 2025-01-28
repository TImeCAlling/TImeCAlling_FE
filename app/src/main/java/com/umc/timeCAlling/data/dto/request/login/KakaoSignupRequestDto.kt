package com.umc.timeCAlling.data.dto.request.login

import java.io.Serializable

data class KakaoSignupRequestDto(
    val kakaoAccessToken: String,
    val nickname: String,
    val avgPrepTime: Int,
    val freeTime: String
)
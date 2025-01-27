package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel

interface LoginRepository {
    // 예시(6번)에 BaseResponse였는데 Result로 바꿨더니 오류 떴던 게 해결됨
    suspend fun kakaoLogin(requestModel: KakaoLoginRequestModel): Result<KakaoLoginResponseModel>
}
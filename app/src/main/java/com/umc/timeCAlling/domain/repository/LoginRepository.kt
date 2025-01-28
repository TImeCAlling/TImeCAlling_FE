package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel

interface LoginRepository {
    suspend fun kakaoLogin(requestModel: KakaoLoginRequestModel): Result<KakaoLoginResponseModel>
    suspend fun kakaoSignup(proflieImage:String, requestModel: KakaoSignupRequestModel): Result<KakaoSignupResponseModel>

}
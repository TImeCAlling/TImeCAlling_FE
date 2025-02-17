package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.request.login.TokenRefreshRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel
import com.umc.timeCAlling.domain.model.response.login.TokenRefreshResponseModel
import okhttp3.MultipartBody

interface LoginRepository {
    suspend fun kakaoLogin(requestModel: KakaoLoginRequestModel): Result<KakaoLoginResponseModel>
    suspend fun kakaoSignup(proflieImage:MultipartBody.Part, requestModel: KakaoSignupRequestModel): Result<KakaoSignupResponseModel>
    suspend fun tokenRefresh(requestModel: TokenRefreshRequestModel): Result<TokenRefreshResponseModel>

}
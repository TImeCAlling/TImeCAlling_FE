package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.request.login.TokenRefreshRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel
import com.umc.timeCAlling.domain.model.response.login.TokenRefreshResponseModel
import com.umc.timeCAlling.domain.repository.LoginRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override suspend fun kakaoLogin(requestModel: KakaoLoginRequestModel): Result<KakaoLoginResponseModel> =
        runCatching {
            loginDataSource.kakaoLogin(requestModel.toKakaoLoginRequestDto()).result.toKakaoLoginResponseModel()
        }

    override suspend fun kakaoSignup(profileImage:MultipartBody.Part,requestModel: KakaoSignupRequestModel): Result<KakaoSignupResponseModel> =
        runCatching {
            loginDataSource.kakaoSignup(profileImage, requestModel.toKakaoSignupRequestDto()).result.toKakaoSignupResponseModel()
        }

    override suspend fun tokenRefresh(requestModel: TokenRefreshRequestModel): Result<TokenRefreshResponseModel> =
        runCatching {
            loginDataSource.tokenRefresh(requestModel.toTokenRefreshRequestDto()).result.toTokenRefreshResponseModel()
        }
}
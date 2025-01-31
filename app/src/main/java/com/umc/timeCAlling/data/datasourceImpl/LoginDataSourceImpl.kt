package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLoginRequestDto
import com.umc.timeCAlling.data.dto.request.login.KakaoSignupRequestDto
import com.umc.timeCAlling.data.dto.request.login.TokenRefreshRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.dto.response.login.KakaoSignupResponseDto
import com.umc.timeCAlling.data.dto.response.login.TokenRefreshResponseDto
import com.umc.timeCAlling.data.service.LoginService
import okhttp3.MultipartBody
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
): LoginDataSource {
    override suspend fun kakaoLogin(requestDto: KakaoLoginRequestDto): BaseResponse<KakaoLoginResponseDto> =
        loginService.kakaoLogin(requestDto)

    override suspend fun kakaoSignup(profileImage: MultipartBody.Part, requestDto: KakaoSignupRequestDto): BaseResponse<KakaoSignupResponseDto> =
        loginService.kakaoSignup(profileImage,requestDto)

    override suspend fun tokenRefresh(requestDto: TokenRefreshRequestDto): BaseResponse<TokenRefreshResponseDto> =
        loginService.tokenRefresh(requestDto)
}
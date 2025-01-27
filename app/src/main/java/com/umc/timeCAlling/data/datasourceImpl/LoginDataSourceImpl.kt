package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.login.KakaoLogintRequestDto
import com.umc.timeCAlling.data.dto.response.login.KakaoLoginResponseDto
import com.umc.timeCAlling.data.service.LoginService
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
): LoginDataSource {
    override suspend fun kakaoLogin(requestDto: KakaoLogintRequestDto): BaseResponse<KakaoLoginResponseDto> = loginService.kakaoLogin(requestDto)
}
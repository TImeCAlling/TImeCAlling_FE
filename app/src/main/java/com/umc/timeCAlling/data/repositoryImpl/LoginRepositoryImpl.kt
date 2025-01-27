package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.LoginDataSource
import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override suspend fun kakaoLogin(requestModel: KakaoLoginRequestModel): Result<KakaoLoginResponseModel> =
        runCatching {
            // result를 올바르게 접근하도록 수정
            val response = loginDataSource.kakaoLogin(requestModel.toKakaoLoginRequestDto())
            response.result.toKakaoLoginResponseModel() // .result에 접근 후 변환
        }
}
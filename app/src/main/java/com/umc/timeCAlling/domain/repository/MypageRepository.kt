package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.request.login.KakaoLoginRequestModel
import com.umc.timeCAlling.domain.model.request.login.KakaoSignupRequestModel
import com.umc.timeCAlling.domain.model.request.mypage.UpdateUserRequestModel
import com.umc.timeCAlling.domain.model.response.login.KakaoLoginResponseModel
import com.umc.timeCAlling.domain.model.response.login.KakaoSignupResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.DeleteUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.UpdateUserResponseModel
import okhttp3.MultipartBody

interface MypageRepository {
    suspend fun getUser(): Result<GetUserResponseModel>
    suspend fun updateUser(proflieImage:MultipartBody.Part?, requestModel: UpdateUserRequestModel): Result<UpdateUserResponseModel>
    suspend fun deleteUser(): Result<DeleteUserResponseModel>
}
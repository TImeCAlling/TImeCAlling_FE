package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.response.mypage.DeleteUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.LogoutUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.UpdateUserResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MypageRepository {
    suspend fun getUser(): Result<GetUserResponseModel>
    suspend fun updateUser(proflieImage:MultipartBody.Part?, requestBody: RequestBody): Result<UpdateUserResponseModel>
    suspend fun deleteUser(): Result<DeleteUserResponseModel>
    suspend fun logoutUser(): Result<LogoutUserResponseModel>
}
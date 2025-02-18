package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.MypageDataSource
import com.umc.timeCAlling.domain.model.request.mypage.UpdateUserRequestModel
import com.umc.timeCAlling.domain.model.response.mypage.DeleteUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.GetUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.LogoutUserResponseModel
import com.umc.timeCAlling.domain.model.response.mypage.UpdateUserResponseModel
import com.umc.timeCAlling.domain.repository.MypageRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MypageRepository {

    override suspend fun getUser(): Result<GetUserResponseModel> =
        runCatching {
            mypageDataSource.getUser().result.toGetUserResponseModel()
        }

    override suspend fun updateUser(
        profileImage: MultipartBody.Part?,
        requestBody: RequestBody // 변경됨
    ): Result<UpdateUserResponseModel> =
        runCatching {
            mypageDataSource.updateUser(profileImage, requestBody).result.toUpdateUserResponseModel()
        }


    override suspend fun deleteUser(): Result<DeleteUserResponseModel> =
        runCatching {
            mypageDataSource.deleteUser().result.toDeleteUserResponseModel()
        }

    override suspend fun logoutUser(): Result<LogoutUserResponseModel> =
        runCatching {
            mypageDataSource.logoutUser().result.toLogoutUserResponseModel()
        }
}

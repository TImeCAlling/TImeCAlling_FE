package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.MypageDataSource
import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.mypage.UpdateUserRequestDto
import com.umc.timeCAlling.data.dto.response.mypage.DeleteUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.GetUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.LogoutUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.UpdateUserResponseDto
import com.umc.timeCAlling.data.service.MypageService
import okhttp3.MultipartBody
import javax.inject.Inject

class MypageDataSourceImpl @Inject constructor(
    private val mypageService: MypageService
): MypageDataSource {
    override suspend fun getUser(): BaseResponse<GetUserResponseDto> =
        mypageService.getUser()

    override suspend fun updateUser(profileImage: MultipartBody.Part?, requestDto: UpdateUserRequestDto): BaseResponse<UpdateUserResponseDto> =
        mypageService.updateUser(profileImage, requestDto)

    override suspend fun deleteUser(): BaseResponse<DeleteUserResponseDto> =
        mypageService.deleteUser()

    override suspend fun logoutUser(): BaseResponse<LogoutUserResponseDto> =
        mypageService.logoutUser()
}

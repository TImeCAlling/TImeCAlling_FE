package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.mypage.UpdateUserRequestDto
import com.umc.timeCAlling.data.dto.response.mypage.DeleteUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.GetUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.UpdateUserResponseDto
import okhttp3.MultipartBody

interface MypageDataSource {
    suspend fun getUser(): BaseResponse<GetUserResponseDto>
    suspend fun updateUser(profileImage:MultipartBody.Part, requestDto:UpdateUserRequestDto): BaseResponse<UpdateUserResponseDto>
    suspend fun deleteUser(): BaseResponse<DeleteUserResponseDto>
}

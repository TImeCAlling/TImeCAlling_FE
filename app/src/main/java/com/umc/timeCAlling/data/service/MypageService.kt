package com.umc.timeCAlling.data.service

import com.umc.timeCAlling.data.dto.BaseResponse
import com.umc.timeCAlling.data.dto.request.mypage.UpdateUserRequestDto
import com.umc.timeCAlling.data.dto.response.mypage.DeleteUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.GetUserResponseDto
import com.umc.timeCAlling.data.dto.response.mypage.UpdateUserResponseDto
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface MypageService {
    @GET("/api/users")
    suspend fun getUser(): BaseResponse<GetUserResponseDto>

    @Multipart
    @PUT("/api/users")
    suspend fun updateUser(
        @Part profileImage: MultipartBody.Part,
        @Part("request") request: UpdateUserRequestDto
    ): BaseResponse<UpdateUserResponseDto>

    @DELETE("/api/users")
    suspend fun deleteUser(): BaseResponse<DeleteUserResponseDto>
}

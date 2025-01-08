package com.umc.timeCAlling.data.service


import com.umc.timeCAlling.data.dto.request.tmap.CarTransportationRequestDto
import com.umc.timeCAlling.data.dto.request.tmap.PublicTransportationRequestDto
import com.umc.timeCAlling.data.dto.request.tmap.WalkTransportationRequestDto
import com.umc.timeCAlling.data.dto.response.tmap.CarTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.PublicTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.WalkTransportationResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TmapService {
    @POST("https://apis.openapi.sk.com/tmap/routes")
    suspend fun getCarTransportation(
        @Body request: CarTransportationRequestDto
    ): CarTransportationResponseDto

    @POST("https://apis.openapi.sk.com/tmap/routes/pedestrian")
    suspend fun getWalkTransportation(
        @Body request: WalkTransportationRequestDto
    ):WalkTransportationResponseDto

    @POST("https://apis.openapi.sk.com/transit/routes")
    suspend fun getPublicTransportation(
        @Body request:PublicTransportationRequestDto
    ):PublicTransportationResponseDto
}
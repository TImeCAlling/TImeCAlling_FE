package com.umc.timeCAlling.data.service


import com.umc.timeCAlling.data.dto.request.tmap.CarTransportationRequestDto
import com.umc.timeCAlling.data.dto.response.tmap.CarTransportationResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TmapService {
    @POST("https://apis.openapi.sk.com/tmap/routes")
    suspend fun getCarTransportation(
        @Body request: CarTransportationRequestDto
    ): CarTransportationResponseDto

    @POST("https://apis.openapi.sk.com/tmap/routes/pedestrian")
    suspend fun getWalkTransportation(
        @Body request: CarTransportationRequestDto
    )
}
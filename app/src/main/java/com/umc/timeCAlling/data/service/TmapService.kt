package com.umc.timeCAlling.data.service


import com.umc.timeCAlling.data.dto.request.tmap.TmapRouteRequestDto
import com.umc.timeCAlling.data.dto.response.tmap.TmapRouteResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TmapService {
    @POST("https://apis.openapi.sk.com/tmap/routes\n")
    suspend fun getCarTransportation(
        @Body request: TmapRouteRequestDto
    ): TmapRouteResponseDto
}
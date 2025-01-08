package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.response.tmap.CarTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.PublicTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.WalkTransportationResponseDto

interface TmapDataSource {
    suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): CarTransportationResponseDto

    suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): WalkTransportationResponseDto

    suspend fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double): PublicTransportationResponseDto
}

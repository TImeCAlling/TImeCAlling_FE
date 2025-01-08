package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.response.tmap.CarTransportationResponseDto

interface TmapDataSource {
    suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): CarTransportationResponseDto
}

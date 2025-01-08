package com.umc.timeCAlling.data.datasource

import com.umc.timeCAlling.data.dto.response.tmap.TmapRouteResponseDto

interface TmapDataSource {
    suspend fun getRoute(startX: Double, startY: Double, endX: Double, endY: Double): TmapRouteResponseDto
}

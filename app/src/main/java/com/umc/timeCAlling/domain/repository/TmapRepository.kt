package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.response.TmapRouteModel


interface TmapRepository {
    suspend fun getRoute(startX: Double, startY: Double, endX: Double, endY: Double): Result<TmapRouteModel>
}
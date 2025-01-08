package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.dto.request.tmap.TmapRouteRequestDto
import com.umc.timeCAlling.data.dto.response.tmap.TmapRouteResponseDto
import com.umc.timeCAlling.data.service.TmapService
import javax.inject.Inject

class TmapDataSourceImpl @Inject constructor(
    private val tmapService: TmapService
) : TmapDataSource {

    override suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): TmapRouteResponseDto =
         tmapService.getCarTransportation(TmapRouteRequestDto(startX, startY, endX, endY))
}
package com.umc.timeCAlling.data.datasourceImpl

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.data.dto.request.tmap.CarTransportationRequestDto
import com.umc.timeCAlling.data.dto.request.tmap.PublicTransportationRequestDto
import com.umc.timeCAlling.data.dto.request.tmap.WalkTransportationRequestDto
import com.umc.timeCAlling.data.dto.response.tmap.CarTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.PublicTransportationResponseDto
import com.umc.timeCAlling.data.dto.response.tmap.WalkTransportationResponseDto
import com.umc.timeCAlling.data.service.TmapService
import javax.inject.Inject

class TmapDataSourceImpl @Inject constructor(
    private val tmapService: TmapService
) : TmapDataSource {

    override suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): CarTransportationResponseDto =
         tmapService.getCarTransportation(CarTransportationRequestDto(startX, startY, endX, endY))

    override suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): WalkTransportationResponseDto =
        tmapService.getWalkTransportation(WalkTransportationRequestDto(startX, startY, endX, endY))

    override suspend fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double): PublicTransportationResponseDto =
        tmapService.getPublicTransportation(PublicTransportationRequestDto(startX, startY, endX, endY))


}

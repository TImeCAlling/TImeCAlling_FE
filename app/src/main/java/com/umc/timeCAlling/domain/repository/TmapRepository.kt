package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.response.CarTransportationModel


interface TmapRepository {
    suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<CarTransportationModel>
}
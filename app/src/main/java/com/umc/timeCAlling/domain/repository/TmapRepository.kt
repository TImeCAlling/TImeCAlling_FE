package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel


interface TmapRepository {
    suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<CarTransportationModel>

    suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<WalkTransportationModel>
}
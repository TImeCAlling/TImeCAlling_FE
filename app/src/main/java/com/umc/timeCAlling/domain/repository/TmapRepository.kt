package com.umc.timeCAlling.domain.repository

import com.umc.timeCAlling.domain.model.response.tmap.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.WalkTransportationModel


interface TmapRepository {
    suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<CarTransportationModel>

    suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<WalkTransportationModel>

    suspend fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<PublicTransportationModel>
}
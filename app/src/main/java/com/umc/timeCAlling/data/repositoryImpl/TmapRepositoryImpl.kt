package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.domain.model.response.tmap.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.tmap.WalkTransportationModel
import com.umc.timeCAlling.domain.repository.TmapRepository
import javax.inject.Inject

class TmapRepositoryImpl @Inject constructor(
    private val tmapDataSource: TmapDataSource
) : TmapRepository {

    override suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<CarTransportationModel> =
        runCatching {tmapDataSource.getCarTransportation(startX,startY,endX,endY).toCarTransportationModel()}

    override suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<WalkTransportationModel> =
        runCatching { tmapDataSource.getWalkTransportation(startX, startY, endX, endY).toWalkTransportationModel() }

    override suspend fun getPublicTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<PublicTransportationModel> =
        runCatching { tmapDataSource.getPublicTransportation(startX, startY, endX, endY).toPublicTransportationModel() }
}
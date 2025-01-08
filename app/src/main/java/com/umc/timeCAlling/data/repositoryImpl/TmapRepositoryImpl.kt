package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel
import com.umc.timeCAlling.domain.repository.TmapRepository
import javax.inject.Inject

class TmapRepositoryImpl @Inject constructor(
    private val tmapDataSource: TmapDataSource
) : TmapRepository {

    override suspend fun getCarTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<CarTransportationModel> =
        runCatching {tmapDataSource.getCarTransportation(startX,startY,endX,endY).toCarTmapRouteModel()}

    override suspend fun getWalkTransportation(startX: Double, startY: Double, endX: Double, endY: Double): Result<WalkTransportationModel> =
        runCatching { tmapDataSource.getWalkTransportation(startX, startY, endX, endY).toWalkTmapRouteModel() }
}
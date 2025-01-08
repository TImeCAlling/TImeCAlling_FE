package com.umc.timeCAlling.data.repositoryImpl

import com.umc.timeCAlling.data.datasource.TmapDataSource
import com.umc.timeCAlling.domain.model.response.TmapRouteModel
import com.umc.timeCAlling.domain.repository.TmapRepository
import javax.inject.Inject

class TmapRepositoryImpl @Inject constructor(
    private val tmapDataSource: TmapDataSource
) : TmapRepository {

    override suspend fun getRoute(startX: Double, startY: Double, endX: Double, endY: Double): Result<TmapRouteModel> =
        runCatching {tmapDataSource.getRoute(startX,startY,endX,endY).toTmapRouteResponseModel()}
    }

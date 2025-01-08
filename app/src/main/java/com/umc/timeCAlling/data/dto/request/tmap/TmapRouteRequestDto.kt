package com.umc.timeCAlling.data.dto.request.tmap

import com.umc.timeCAlling.R
import com.umc.timeCAlling.TimeCAllingApplication.Companion.getString

data class TmapRouteRequestDto(
    val startX: Double,
    val startY: Double,
    val endX: Double,
    val endY: Double,
    val appKey: String = getString(R.string.tmap_app_key),
    val startName: String = "출발지",
    val endName: String = "도착지"
)
package com.umc.timeCAlling.domain.model.response.tmap

data class WalkTransportationModel(
    val type: String,
    val features: List<WalkFeatureModel?>?
) {
    data class WalkFeatureModel(
        val type: String?,
        val geometry: WalkGeometryModel?,
        val properties: WalkPropertiesModel?
    ) {
        data class WalkGeometryModel(
            val type: String?,
            val coordinates: Any?
        )
        data class WalkPropertiesModel(
            val totalDistance: Int?,
            val totalTime: Int?,
            val index: Int?,
            val pointIndex: Int?,
            val name: String?,
            val description: String?,
            val direction: String?,
            val nearPoiName: String?,
            val nearPoiX: String?,
            val nearPoiY: String?,
            val intersectionName:String?,
            val facilityType: String?,
            val facilityName: String?,
            val turnType: Int?,
            val pointType: String?,
            val lineIndex: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val distance: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val time: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val roadType: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val categoryRoadType: Int?
        )
    }
}

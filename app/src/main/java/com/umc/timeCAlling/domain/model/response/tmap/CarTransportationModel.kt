package com.umc.timeCAlling.domain.model.response.tmap

data class CarTransportationModel(
    val type: String, // Add type property
    val features: List<CarFeatureModel?>?
) {
    data class CarFeatureModel(
        val type: String?, // Add type property
        val geometry: CarGeometryModel?,
        val properties: CarPropertiesModel?
    ) {
        data class CarGeometryModel(
            val type: String?,
            val coordinates: Any?,
            val traffic: Any?
        )
        data class CarPropertiesModel(
            val totalDistance: Int?,
            val totalTime: Int?,
            val totalFare: Int?,
            val taxiFare: Int?,
            val index: Int?,
            val pointIndex: Int?,
            val name: String?,
            val description: String?,
            val nextRoadName: String?,
            val turnType: Int?,
            val pointType: String?,
            val lineIndex: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val distance: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val time: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val fare: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val roadType: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val facilityType: Int?, // LineString 타입의 Feature에만 존재하는 속성
            val departIdx: Int?, // rs6 타입의 Feature에만 존재하는 속성
            val destIdx: Int? // rs6 타입의 Feature에만 존재하는 속성
        )
    }
}
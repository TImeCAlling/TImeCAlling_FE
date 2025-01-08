package  com.umc.timeCAlling.data.dto.response.tmap


import com.umc.timeCAlling.domain.model.response.CarTransportationModel

data class CarTransportationResponseDto(
    val type: String,
    val features: List<FeatureResponseDto?>?
) {
    data class FeatureResponseDto(
        val type: String?,
        val geometry: GeometryResponseDto?,
        val properties: PropertiesResponseDto?
    ) {
        data class GeometryResponseDto(
            val type: String?,
            val coordinates: Any?,
            val traffic: Any?
        ){
            fun toGeometryModel()=
                CarTransportationModel.FeatureModel.GeometryModel(type,coordinates,traffic)
        }
        data class PropertiesResponseDto(
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
        ) {
            fun toPropertiesModel()=
                CarTransportationModel.FeatureModel.PropertiesModel(totalDistance, totalTime, totalFare, taxiFare, index, pointIndex, name, description, nextRoadName, turnType, pointType, lineIndex, distance, time, fare, roadType, facilityType, departIdx, destIdx)
        }fun toFeatureModel() =
            CarTransportationModel.FeatureModel(
                type,
                geometry?.toGeometryModel(),
                properties?.toPropertiesModel()
            )
    }
    fun toTmapRouteModel() =
        CarTransportationModel(type,features?.map {  it?.toFeatureModel() } ?: emptyList())
}

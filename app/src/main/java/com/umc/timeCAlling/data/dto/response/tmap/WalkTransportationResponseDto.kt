package  com.umc.timeCAlling.data.dto.response.tmap


import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel

data class WalkTransportationResponseDto(
    val type: String,
    val features: List<WalkFeatureResponseDto?>?
) {
    data class WalkFeatureResponseDto(
        val type: String?,
        val geometry: WalkGeometryResponseDto?,
        val properties: WalkPropertiesResponseDto?
    ) {
        data class WalkGeometryResponseDto(
            val type: String?,
            val coordinates: Any?
        ){
            fun toWalkGeometryModel()=
                WalkTransportationModel.WalkFeatureModel.WalkGeometryModel(type,coordinates)
        }
        data class WalkPropertiesResponseDto(
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
        ) {
            fun toWalkPropertiesModel()=
                WalkTransportationModel.WalkFeatureModel.WalkPropertiesModel(totalDistance, totalTime, index, pointIndex, name, description, direction, nearPoiName, nearPoiX, nearPoiY, intersectionName, facilityType, facilityName, turnType, pointType, lineIndex, distance, time, roadType, categoryRoadType)
        }fun toWalkFeatureModel() =
            WalkTransportationModel.WalkFeatureModel(type, geometry?.toWalkGeometryModel(), properties?.toWalkPropertiesModel())
    }
    fun toWalkTransportationModel() =
        WalkTransportationModel(type,features?.map {  it?.toWalkFeatureModel() } ?: emptyList())
}

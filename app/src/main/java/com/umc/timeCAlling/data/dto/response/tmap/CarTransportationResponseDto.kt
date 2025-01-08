package  com.umc.timeCAlling.data.dto.response.tmap


import com.umc.timeCAlling.domain.model.response.CarTransportationModel

data class CarTransportationResponseDto(
    val type: String,
    val features: List<CarFeatureResponseDto?>?
) {
    data class CarFeatureResponseDto(
        val type: String?,
        val geometry: CarGeometryResponseDto?,
        val properties: CarPropertiesResponseDto?
    ) {
        data class CarGeometryResponseDto(
            val type: String?,
            val coordinates: Any?,
            val traffic: Any?
        ){
            fun toCarGeometryModel()=
                CarTransportationModel.CarFeatureModel.CarGeometryModel(type,coordinates,traffic)
        }
        data class CarPropertiesResponseDto(
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
            fun toCarPropertiesModel()=
                CarTransportationModel.CarFeatureModel.CarPropertiesModel(totalDistance, totalTime, totalFare, taxiFare, index, pointIndex, name, description, nextRoadName, turnType, pointType, lineIndex, distance, time, fare, roadType, facilityType, departIdx, destIdx)
        }fun toCarFeatureModel() =
            CarTransportationModel.CarFeatureModel(
                type,
                geometry?.toCarGeometryModel(),
                properties?.toCarPropertiesModel()
            )
    }
    fun toCarTmapRouteModel() =
        CarTransportationModel(type,features?.map {  it?.toCarFeatureModel() } ?: emptyList())
}

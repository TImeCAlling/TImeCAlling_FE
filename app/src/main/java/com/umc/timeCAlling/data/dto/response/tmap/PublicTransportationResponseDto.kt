package  com.umc.timeCAlling.data.dto.response.tmap


import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.PublicTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel

data class PublicTransportationResponseDto(
    val metaData: PublicMetaDataResponseDto?,
) {
    fun toPublicTransportationModel() =
        PublicTransportationModel(metaData?.toPublicMetaDataModel())

    data class PublicMetaDataResponseDto(
        val requestParameters: PublicRequestParametersResponseDto?,
        val plan: PublicPlanResponseDto?
    ) {
        fun toPublicMetaDataModel() =
            PublicTransportationModel.PublicMetaDataModel(requestParameters?.toPublicRequestParametersModel(), plan?.toPublicPlanModel())

        data class PublicRequestParametersResponseDto(
            val busCount: Int?,
            val expressbusCount: Int?,
            val subwayCount: Int?,
            val airplaneCount: Int?,
            val locale: String?,
            val endY: String?,
            val endX: String?,
            val wideareaRouteCount: Int?,
            val subwayBusCount: Int?,
            val startX: String?,
            val startY: String?,
            val ferryCount: Int?,
            val trainCount: Int?,
            val reqDttm: String?
        ){
            fun toPublicRequestParametersModel() =
                PublicTransportationModel.PublicMetaDataModel.PublicRequestParametersModel(busCount, expressbusCount, subwayCount, airplaneCount, locale, endY, endX, wideareaRouteCount, subwayBusCount, startX, startY, ferryCount, trainCount, reqDttm)
        }

        data class PublicPlanResponseDto(
            val itineraries: List<PublicItineraryResponseDto?>?
        ) {
            fun toPublicPlanModel() =
                PublicTransportationModel.PublicMetaDataModel.PublicPlanModel(itineraries?.map { it?.toPublicItineraryModel() })

            data class PublicItineraryResponseDto(
                val fare: PublicFareResponseDto?,
                val totalTime: Int?,
                val legs: List<PublicLegResponseDto?>?
            ) {
                fun toPublicItineraryModel() =
                    PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel(fare?.toPublicFareModel(), totalTime, legs?.map { it?.toPublicLegModel() })

                data class PublicFareResponseDto(
                    val regular: PublicRegularResponseDto?
                ) {
                    fun toPublicFareModel() =
                        PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicFareModel(regular?.toPublicRegularModel())

                    data class PublicRegularResponseDto(
                        val totalFare: Int?,
                        val currency: PublicCurrencyResponseDto?
                    ) {
                        fun toPublicRegularModel() =
                            PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicFareModel.PublicRegularModel(totalFare, currency?.toPublicCurrencyModel())

                        data class PublicCurrencyResponseDto(
                            val symbol: String?,
                            val currency: String?,
                            val currencyCode: String?
                        ){
                            fun toPublicCurrencyModel() =
                                PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicFareModel.PublicRegularModel.PublicCurrencyModel(symbol, currency, currencyCode)
                        }
                    }
                }

                data class PublicLegResponseDto(
                    val mode: String?,
                    val routeColor:String?,
                    val sectionTime: Int?,
                    val distance: Int?,
                    val route: String?,
                    val start: PublicStartResponseDto?,
                    val end: PublicEndResponseDto?,
                    val step: List<PublicStepResponseDto?>?
                ) {
                    fun toPublicLegModel() =
                        PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicLegModel(mode,routeColor, sectionTime, route,distance, start?.toPublicStartModel(), end?.toPublicEndModel(), step?.map { it?.toPublicStepModel() })

                    data class PublicStartResponseDto(
                        val name: String?,
                        val lon: Double?,
                        val lat: Double?
                    ){
                        fun toPublicStartModel() =
                            PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicLegModel.PublicStartModel(name, lon, lat)
                    }

                    data class PublicEndResponseDto(
                        val name: String?,
                        val lon: Double?,
                        val lat: Double?
                    ){
                        fun toPublicEndModel() =
                            PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicLegModel.PublicEndModel(name, lon, lat)
                    }

                    data class PublicStepResponseDto(
                        val streetName: String?,
                        val distance: Int?,
                        val description: String?,
                        val linestring: String?
                    ){
                        fun toPublicStepModel() =
                            PublicTransportationModel.PublicMetaDataModel.PublicPlanModel.PublicItineraryModel.PublicLegModel.PublicStepModel(streetName, distance, description, linestring)
                    }
                }
            }
        }
    }
}
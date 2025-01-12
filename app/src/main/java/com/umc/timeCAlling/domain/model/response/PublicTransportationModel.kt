package  com.umc.timeCAlling.domain.model.response


import com.umc.timeCAlling.domain.model.response.CarTransportationModel
import com.umc.timeCAlling.domain.model.response.WalkTransportationModel

data class PublicTransportationModel(
    val metaData: PublicMetaDataModel?,
) {
    data class PublicMetaDataModel(
        val requestParameters: PublicRequestParametersModel?,
        val plan: PublicPlanModel?
    ) {
        data class PublicRequestParametersModel(
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
        )

        data class PublicPlanModel(
            val itineraries: List<PublicItineraryModel?>?
        ) {
            data class PublicItineraryModel(
                val fare: PublicFareModel?,
                val totalTime: Int?,
                val legs: List<PublicLegModel?>?
            ) {
                data class PublicFareModel(
                    val regular: PublicRegularModel?
                ) {
                    data class PublicRegularModel(
                        val totalFare: Int?,
                        val currency: PublicCurrencyModel?
                    ) {
                        data class PublicCurrencyModel(
                            val symbol: String?,
                            val currency: String?,
                            val currencyCode: String?
                        )
                    }
                }

                data class PublicLegModel(
                    val mode: String?,
                    val routeColor:String?,
                    val sectionTime: Int?,
                    val route: String?,
                    val distance: Int?,
                    val start: PublicStartModel?,
                    val end: PublicEndModel?,
                    val step: List<PublicStepModel?>?
                ) {
                    data class PublicStartModel(
                        val name: String?,
                        val lon: Double?,
                        val lat: Double?
                    )

                    data class PublicEndModel(
                        val name: String?,
                        val lon: Double?,
                        val lat: Double?
                    )

                    data class PublicStepModel(
                        val streetName: String?,
                        val distance: Int?,
                        val description: String?,
                        val linestring: String?
                    )
                }
            }
        }
    }
}
package org.sagebionetworks.assessmentmodel.passivedata.weather

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class AirQualityServiceResult(
    val identifier: String,
    val providerName: String,
    val startDate: Instant,
    val aqi: Int?,
    val category: String?
) {
    val serviceType = WeatherServiceType.AirQuality
}

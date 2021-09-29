package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.ResultData

@Serializable
data class WeatherResult(
    override val identifier: String,
    override val startDate: Instant,
    override val endDate: Instant,
    val weather: WeatherServiceResult?,
    val airQualityServiceResult: AirQualityServiceResult?
) : ResultData {
}
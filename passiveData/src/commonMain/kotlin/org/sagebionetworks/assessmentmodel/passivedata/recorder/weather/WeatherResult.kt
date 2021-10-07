package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.OffsetZonedInstantSerializer
import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import org.sagebionetworks.assessmentmodel.passivedata.recorder.weather.WeatherServiceTypeStrings.TYPE_WEATHER

@Serializable
@SerialName(TYPE_WEATHER)
data class WeatherResult(
    override val identifier: String,
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val startDate: Instant = Clock.System.now(),
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val endDate: Instant = Clock.System.now(),
    val weather: WeatherServiceResult?,
    val airQuality: AirQualityServiceResult?
) : ResultData {
}


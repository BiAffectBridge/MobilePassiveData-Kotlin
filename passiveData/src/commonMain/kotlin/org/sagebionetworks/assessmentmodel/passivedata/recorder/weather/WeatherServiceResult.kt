package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.OffsetZonedInstantSerializer
import org.sagebionetworks.assessmentmodel.passivedata.ResultData

@Serializable
@SerialName(WeatherServiceTypeStrings.TYPE_WEATHER)
data class WeatherServiceResult(
    override val identifier: String,
    @SerialName("provider")
    val providerName: WeatherServiceProviderName,
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val startDate: Instant,
    val temperature: Double? = null,
    val seaLevelPressure: Double? = null,
    val groundLevelPressure: Double? = null,
    val humidity: Double? = null,
    val clouds: Double? = null,
    val rain: Precipitation? = null,
    val snow: Precipitation? = null,
    val wind: Wind? = null
) : ResultData {
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val endDate: Instant
        get() = startDate

    @Serializable
    data class Precipitation(val pastHour: Double? = null, val pastThreeHours: Double? = null)

    @Serializable
    data class Wind(val speed: Double, val degrees: Double? = null, val gust: Double? = null)
}


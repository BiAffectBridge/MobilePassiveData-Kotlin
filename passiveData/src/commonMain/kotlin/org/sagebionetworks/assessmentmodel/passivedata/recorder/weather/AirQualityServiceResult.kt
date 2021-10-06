package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.OffsetZonedInstantSerializer
import org.sagebionetworks.assessmentmodel.passivedata.ResultData
import org.sagebionetworks.assessmentmodel.passivedata.recorder.weather.WeatherServiceTypeStrings.TYPE_AIR_QUALITY

@Serializable
@SerialName(TYPE_AIR_QUALITY)
data class AirQualityServiceResult(
    override val identifier: String,
    @SerialName("provider")
    val providerName: WeatherServiceProviderName,
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val startDate: Instant,
    val aqi: Int?,
    val category: Category?
) : ResultData {
    @Serializable(with = OffsetZonedInstantSerializer::class)
    override val endDate: Instant
        get() = startDate

    @Serializable
    data class Category(
        val number: Int,
        val name: String
    )
}

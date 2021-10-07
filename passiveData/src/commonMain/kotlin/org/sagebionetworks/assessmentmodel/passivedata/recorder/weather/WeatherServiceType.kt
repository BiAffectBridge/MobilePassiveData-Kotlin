package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.internal.StringEnum
import org.sagebionetworks.assessmentmodel.passivedata.recorder.weather.WeatherServiceTypeStrings.TYPE_AIR_QUALITY
import org.sagebionetworks.assessmentmodel.passivedata.recorder.weather.WeatherServiceTypeStrings.TYPE_WEATHER

@Serializable
enum class WeatherServiceType(override val serialName: String) : StringEnum {

    @SerialName(TYPE_WEATHER)
    Weather(TYPE_WEATHER),

    @SerialName(TYPE_AIR_QUALITY)
    AirQuality(TYPE_AIR_QUALITY);
}

object WeatherServiceTypeStrings {
    const val TYPE_WEATHER = "weather"
    const val TYPE_AIR_QUALITY = "airQuality"
}
package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.internal.StringEnum

@Serializable
enum class WeatherServiceType : StringEnum {
    Weather,
    AirQuality
}
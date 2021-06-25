package org.sagebionetworks.assessmentmodel.passivedata.weather

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.asyncactions.StringEnum

@Serializable
enum class WeatherServiceType : StringEnum {
    Weather,
    AirQuality
}
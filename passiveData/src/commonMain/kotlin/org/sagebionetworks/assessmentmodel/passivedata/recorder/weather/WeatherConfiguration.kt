package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.internal.StringEnum

@Serializable
data class WeatherConfiguration(
    override val identifier: String,
    override val typeName: String,
    override val startStepIdentifier: String?,
    val services: List<WeatherServiceConfiguration>
) : AsyncActionConfiguration {
}

@Serializable
data class WeatherServiceConfiguration(
    val identifier: String,
    val providerName: WeatherServiceProviderName,
    val apiKey: String
)

@Serializable
enum class WeatherServiceProviderName(val serializedName: String) : StringEnum {
    @SerialName("airNow")
    AIR_NOW("airNow"),

    @SerialName("openWeather")
    OPEN_WEATHER("openWeather");

    companion object {
        fun findForSerializedName(serializedName: String): WeatherServiceProviderName? {
            return WeatherServiceProviderName.values().find {
                it.serializedName == serializedName
            }
        }

    }
}

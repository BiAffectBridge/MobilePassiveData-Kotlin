package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.asyncActionConfigurationSerializersModule
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherConfigurationTest {

    val jsonCoder = Json {
        serializersModule += asyncActionConfigurationSerializersModule
        ignoreUnknownKeys = true
    }

    @Test
    fun testDeserialization() {
        val result = jsonCoder.decodeFromString<WeatherConfiguration>(sampleJson)

        with(result) {
            assertEquals("weather", identifier)
            assertEquals(WeatherConfiguration.TYPE, typeName)
            with(services[0]) {
                assertEquals("airQuality", identifier)
                assertEquals(WeatherServiceProviderName.AIR_NOW, providerName)
                assertEquals("airNowKey", apiKey)
            }

            with(services[1]) {
                assertEquals("weather", identifier)
                assertEquals(WeatherServiceProviderName.OPEN_WEATHER, providerName)
                assertEquals("openWeatherKey", apiKey)
            }
        }
    }
}

// json from Bridge's BackgroundRecorders Configuration Element
val sampleJson =
    """
{
  "identifier": "weather",
  "type": "weather",
  "services": [
    {
      "identifier": "airQuality",
      "type": "airNow",
      "provider": "airNow",
      "key": "airNowKey"
    },
    {
      "identifier": "weather",
      "type": "openWeather",
      "provider": "openWeather",
      "key": "openWeatherKey"
    }
  ]
}
    """.trimIndent()
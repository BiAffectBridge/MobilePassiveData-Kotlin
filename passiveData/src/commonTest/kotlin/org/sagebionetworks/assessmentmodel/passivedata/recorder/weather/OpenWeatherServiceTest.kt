package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenWeatherServiceTest {
    private val jsonCoder = OpenWeatherService.Response.jsonCoder

    @Test
    fun testDeserialize() {
        val result = jsonCoder.decodeFromString<OpenWeatherService.Response>(sampleJson)

        with(result) {
            assertEquals(1560350645, dt.epochSeconds)
            assertEquals(1023.0, main.seaLevel)
        }
    }

    val sampleJson = """
    {
      "coord": {
        "lon": -122.08,
        "lat": 37.39
      },
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "clear sky",
          "icon": "01d"
        }
      ],
      "base": "stations",
      "main": {
        "temp": 282.55,
        "feels_like": 281.86,
        "temp_min": 280.37,
        "temp_max": 284.26,
        "pressure": 1023,
        "humidity": 100
      },
      "visibility": 16093,
      "wind": {
        "speed": 1.5,
        "deg": 350
      },
      "clouds": {
        "all": 1
      },
      "dt": 1560350645,
      "sys": {
        "type": 1,
        "id": 5122,
        "message": 0.0139,
        "country": "US",
        "sunrise": 1560343627,
        "sunset": 1560396563
      },
      "timezone": -25200,
      "id": 420006353,
      "name": "Mountain View",
      "cod": 200
      }
    """

}
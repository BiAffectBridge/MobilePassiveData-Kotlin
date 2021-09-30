package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AirQualityServiceTest {
    private val jsonCoder = OpenWeatherService.Response.jsonCoder

    @Test
    fun testDeserialize() {
        val result = jsonCoder.decodeFromString<List<AirQualityService.Response>>(sampleJson)

        assertEquals(3, result.size)
        with(result[1]) {
            assertEquals("CA", stateCode)
            assertEquals(57, aqi)

            assertNotNull(category)
            assertEquals(2, category!!.number)
            assertEquals("Moderate", category!!.name)
        }
    }

    val sampleJson = """
  [{
  		"DateIssue": "2020-11-20 ",
  		"DateForecast": "2020-11-20 ",
  		"ReportingArea": "Yuba City/Marysville",
  		"StateCode": "CA",
  		"Latitude": 39.1389,
  		"Longitude": -121.6175,
  		"ParameterName": "PM2.5",
  		"AQI": 46,
  		"Category": {
  			"Number": 1,
  			"Name": "Good"
  		},
  		"ActionDay": false,
  		"Discussion": "Friday through Sunday, a weak upper-level ridge of high pressure over northern California will reduce vertical mixing in Yuba and Sutter Counties. In addition, light northwesterly winds will limit pollutant dispersion. These conditions will cause AQI levels to increase from high-Good Friday to Moderate over the weekend."
  	},
  	{
  		"DateIssue": "2020-11-20 ",
  		"DateForecast": "2020-11-21 ",
  		"ReportingArea": "Yuba City/Marysville",
  		"StateCode": "CA",
  		"Latitude": 39.1389,
  		"Longitude": -121.6175,
  		"ParameterName": "PM2.5",
  		"AQI": 57,
  		"Category": {
  			"Number": 2,
  			"Name": "Moderate"
  		},
  		"ActionDay": false,
  		"Discussion": "Friday through Sunday, a weak upper-level ridge of high pressure over northern California will reduce vertical mixing in Yuba and Sutter Counties. In addition, light northwesterly winds will limit pollutant dispersion. These conditions will cause AQI levels to increase from high-Good Friday to Moderate over the weekend."
  	},
  	{
  		"DateIssue": "2020-11-20 ",
  		"DateForecast": "2020-11-22 ",
  		"ReportingArea": "Yuba City/Marysville",
  		"StateCode": "CA",
  		"Latitude": 39.1389,
  		"Longitude": -121.6175,
  		"ParameterName": "PM2.5",
  		"AQI": 66,
  		"Category": {
  			"Number": 2,
  			"Name": "Moderate"
  		},
  		"ActionDay": false,
  		"Discussion": "Friday through Sunday, a weak upper-level ridge of high pressure over northern California will reduce vertical mixing in Yuba and Sutter Counties. In addition, light northwesterly winds will limit pollutant dispersion. These conditions will cause AQI levels to increase from high-Good Friday to Moderate over the weekend."
  	}
  ]
    """

}
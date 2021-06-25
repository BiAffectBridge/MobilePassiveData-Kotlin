package org.sagebionetworks.assessmentmodel.weather

import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.passivedata.weather.AirQualityServiceResult
import kotlin.test.Test
import kotlin.test.assertEquals

class AirQualityServiceResultTest {

    private val jsonCoder = Json

    @Test
    fun testSerialization() {
        val airQualityServiceResult = AirQualityServiceResult(
            "id",
            "someProvider",
            Clock.System.now(),
            110,
            "someCategory"
        )

        val jsonResult = jsonCoder.encodeToString(airQualityServiceResult)

        val deserializedresult = jsonCoder.decodeFromString<AirQualityServiceResult>(jsonResult)

        assertEquals(airQualityServiceResult, deserializedresult)
    }
}
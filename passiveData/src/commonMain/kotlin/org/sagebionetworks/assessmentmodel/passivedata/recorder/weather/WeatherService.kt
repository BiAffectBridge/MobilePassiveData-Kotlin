package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import org.sagebionetworks.assessmentmodel.passivedata.ResultData

interface WeatherService {
    val configuration: WeatherServiceConfiguration
    suspend fun getResult(location: Location): ResultData
}

data class Location(val longitude: Double, val latitude: Double)
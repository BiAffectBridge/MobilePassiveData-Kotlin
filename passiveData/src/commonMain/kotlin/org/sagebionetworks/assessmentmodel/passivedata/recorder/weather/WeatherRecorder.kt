package org.sagebionetworks.assessmentmodel.passivedata.recorder.weather

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionStatus
import org.sagebionetworks.assessmentmodel.passivedata.recorder.Recorder

abstract class WeatherRecorder(override val configuration: WeatherConfiguration) :
    Recorder<WeatherResult> {

    lateinit var weatherServices: List<WeatherService>


    override val status: AsyncActionStatus
        get() = TODO("Not yet implemented")
    override val currentStepPath: String
        get() = TODO("Not yet implemented")

    suspend fun launchWeatherServices(location: Location?) {
        if (location == null) {
            Napier.w("No location available, unable to start recorder")
            return
        }
        supervisorScope {
            weatherServices.map { service ->
                this.async {
                    //TODO: real location
                    return@async service.getResult(location)
                }
            }
        }
    }

    abstract suspend fun getLocation(): Location?

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun isPaused(): Boolean {
        TODO("Not yet implemented")
    }

    override fun stop() {
    }

    override val result = CompletableDeferred<WeatherResult>()

    override fun cancel() {
    }
}
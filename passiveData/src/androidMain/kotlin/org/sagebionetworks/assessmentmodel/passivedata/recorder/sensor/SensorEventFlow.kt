package org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.MotionRecorderConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.MotionRecorderType
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.toSensorType
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class SensorEventFlow(
    val context: Context,
    val sensors: List<Int>,
    val samplingPeriodInUs: Int
) {
    val sensorManager: SensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

    fun getSensorData(): SharedFlow<ChannelSensorEvent> {

        val flow: Flow<ChannelSensorEvent> = callbackFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event !== null) {
                        channel.trySend(ChannelSensorEvent.Event(event))
                    }

                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    if (sensor !== null) {
                        channel.trySend(ChannelSensorEvent.AccuracyChange(sensor, accuracy))
                    }
                }
            }

            sensors.map { sensorType ->
                return@map sensorManager.getDefaultSensor(sensorType)
                    ?: let {
                        Napier.w("Unable to find default sensor of type $sensorType")
                        null
                    }
            }.filterNotNull()
                .forEach { sensor ->
                    Napier.d("Registering listener for sensor of type ${sensor.stringType}")
                    sensorManager.registerListener(
                        listener,
                        sensor,
                        samplingPeriodInUs
                    )
                }

            awaitClose {
                Napier.d("Unregistering SensorEventListener")
                sensorManager.unregisterListener(listener)
            }
        }


        return flow.shareIn(
            CoroutineScope(Dispatchers.IO),
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 0, replayExpirationMillis = 0)
        )
    }

    companion object {
        private val SECONDS_TO_MICROSECONDS = 1_000_000
        private val SENSOR_DELAY_DEFAULT = 10_000 //10,000 microseconds -> 100hz

        fun fromMotionRecorderConfig(
            context: Context,
            motionRecorderConfiguration: MotionRecorderConfiguration
        ): SensorEventFlow {
            val samplingPeriodInUs = motionRecorderConfiguration.frequency?.let {
                (1 / motionRecorderConfiguration.frequency
                        * SECONDS_TO_MICROSECONDS).roundToInt()
                    .toInt()
            } ?: SENSOR_DELAY_DEFAULT

            return SensorEventFlow(
                context,
                (motionRecorderConfiguration.recorderTypes ?: setOf(
                    MotionRecorderType.ACCELEROMETER,
                    MotionRecorderType.GYRO
                ))
                    .map { it.toSensorType() }.filterNotNull(),
                samplingPeriodInUs
            )
        }
    }
}


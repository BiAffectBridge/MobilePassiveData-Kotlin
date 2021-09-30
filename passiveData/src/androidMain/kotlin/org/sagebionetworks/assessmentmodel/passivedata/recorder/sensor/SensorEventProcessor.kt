package org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SensorEventProcessor(
    val context: Context,
    val sensors: List<Sensor>,
    val samplingPeriodInUs: Int
) : SensorEventListener {
    val sensorManager: SensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private val scope = CoroutineScope(Dispatchers.Default)
    private val events = Channel<SensorEvent>(100) // Some backlog capacity

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // If you need to process this as well, it would be a good idea
        // to wrap the values from this as well as onSensorChanged() into
        // a custom SensorEvent class and then put it on a channel.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { offer(it) }
    }

    private fun offer(event: SensorEvent) = runBlocking { events.send(event) }

    private fun process() = scope.launch {
        events.consumeEach {
            // Do something
        }
    }


    fun CoroutineScope.getSensor() = produce<ChannelSensorEvent> {

    }

//    fun test() {
//        val a = callbackFlow<Int> { }<Int> { 1 }
//
//    }

    @ExperimentalCoroutinesApi
    fun getSensorData(): Flow<ChannelSensorEvent> {

        return callbackFlow {
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

            sensors.forEach { sensor ->
                sensorManager.registerListener(
                    listener,
                    sensor,
                    samplingPeriodInUs
                )
            }

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }


}
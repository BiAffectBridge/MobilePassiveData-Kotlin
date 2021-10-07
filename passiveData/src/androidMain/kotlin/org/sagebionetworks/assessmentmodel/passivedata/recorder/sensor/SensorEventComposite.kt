package org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent

/**
 * Composite of the SensorChanged and AccuracyChanged values received from SensorManager.
 *
 * @see android.hardware.SensorEventListener
 */
sealed class SensorEventComposite(
    private val sensorEvent: SensorEvent?,
    private val sensor: Sensor?,
    private val accuracy: Int?
) {
    class SensorChanged(val sensorEvent: SensorEvent) :
        SensorEventComposite(sensorEvent, null, null)

    class AccuracyChange(val sensor: Sensor, val accuracy: Int) :
        SensorEventComposite(null, sensor, accuracy)
}



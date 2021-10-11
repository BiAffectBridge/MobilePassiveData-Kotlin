package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import android.content.Context
import android.hardware.Sensor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor.SensorEventFlow
import kotlin.math.roundToInt

/**
 * Taken from ResearchStack and SageResearch DeviceMotionUtils.
 */
fun MotionRecorderType.toSensorType(): Int? {
    return when (this) {
        MotionRecorderType.ACCELEROMETER, MotionRecorderType.USER_ACCELERATION -> Sensor.TYPE_ACCELEROMETER
        MotionRecorderType.GYRO -> Sensor.TYPE_GYROSCOPE
        MotionRecorderType.MAGNETIC_FIELD, MotionRecorderType.MAGNETOMETER -> Sensor.TYPE_MAGNETIC_FIELD
        MotionRecorderType.GRAVITY -> Sensor.TYPE_GRAVITY
        MotionRecorderType.ROTATION_RATE -> Sensor.TYPE_ROTATION_VECTOR
        MotionRecorderType.ATTITUDE -> null
    }
}

@ExperimentalCoroutinesApi
fun MotionRecorderConfiguration.createMotionRecorder(
    context: Context
): SensorEventFlow {
    val secondsToMicroseconds = 1_000_000
    val sensorDelayDefault = 10_000 //10,000 microseconds -> 100hz

    val samplingPeriodInUs = frequency?.let {
        (1 / frequency * secondsToMicroseconds).roundToInt()
    } ?: sensorDelayDefault

    /// The recorder types to use for this recording. This will be set to the `recorderTypes`
    /// from the `coreMotionConfiguration`. If that value is `nil`, then the defaults are
    /// `[.accelerometer, .gyro]` because all other non-compass measurements can be calculated
    /// from the accelerometer and gyro.

    val sensors = let {
        recorderTypes
            ?: setOf(MotionRecorderType.ACCELEROMETER, MotionRecorderType.GYRO)
    }.map { it.toSensorType() }.filterNotNull()

    return SensorEventFlow(context, sensors, samplingPeriodInUs)
}

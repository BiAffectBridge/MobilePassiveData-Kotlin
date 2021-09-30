package org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.sagebionetworks.assessmentmodel.passivedata.recorder.SampleRecord
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.DeviceMotionUtil
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.DeviceMotionUtil.Companion.SENSOR_TYPE_TO_DATA_TYPE
import org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor.SensorRecord.Companion.SECONDS_TO_NANOS
import kotlin.time.ExperimentalTime

interface SensorRecord
    : SampleRecord {

    val sensorType: String?

    // system uptime in seconds
    val uptime: Double?
    val eventAccuracy: Int?

    // first sensor record will have a timestampDate. this is the zero reference for subsequent
    // records' relative timestamp
    override val timestampDate: Instant?

    // relative timestamp in seconds. seconds elapsed since first recorded event
    override val timestamp: DateTimePeriod?

    companion object {
        const val SECONDS_TO_NANOS = 1_000_000_000
    }
}

@Serializable
data class FirstRecord(
    override val timestampDate: Instant? = null,
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    override val eventAccuracy: Int?,
    @Transient val sensor: Sensor? = null
) : SensorRecord

@ExperimentalTime
fun SensorEvent.createFirstRecord(): FirstRecord {
    return FirstRecord(
        timestampDate = DeviceMotionUtil.SensorEventPOJO.instantOf(timestamp),
        timestamp = DateTimePeriod(),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        eventAccuracy = null,
        sensor = sensor
    )
}

@ExperimentalSerializationApi
@Serializable
data class AccelerationRecord(
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    val x: Double,
    val y: Double,
    val z: Double
) : SensorRecord {
    @Transient
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val unit = "g"

    override val timestampDate: Instant? = null
    override val eventAccuracy: Int? = null
}

@ExperimentalSerializationApi
fun SensorEvent.createAccelerationRecord(referenceTimestamp: Long): AccelerationRecord {
    return AccelerationRecord(
        timestamp = DateTimePeriod(nanoseconds = timestamp - referenceTimestamp),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        x = (values[0] / SensorManager.GRAVITY_EARTH).toDouble(),
        y = (values[1] / SensorManager.GRAVITY_EARTH).toDouble(),
        z = (values[2] / SensorManager.GRAVITY_EARTH).toDouble()
    )
}

@ExperimentalSerializationApi
data class GyroscopeRecord(
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    val x: Double,
    val y: Double,
    val z: Double
) : SensorRecord {
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val unit = "rad/s"

    override val timestampDate: Instant? = null
    override val eventAccuracy: Int? = null
}

@ExperimentalSerializationApi
fun SensorEvent.createGyroscopeRecord(referenceTimestamp: Long): GyroscopeRecord {
    return GyroscopeRecord(
        timestamp = DateTimePeriod(nanoseconds = timestamp - referenceTimestamp),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        x = values[0].toDouble(),
        y = values[1].toDouble(),
        z = values[2].toDouble()
    )
}

@ExperimentalSerializationApi
@Serializable
data class MagneticRecord(
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    val x: Double,
    val y: Double,
    val z: Double
) : SensorRecord {
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val unit = "uT"

    override val timestampDate: Instant? = null
    override val eventAccuracy: Int? = null
}

@ExperimentalSerializationApi
fun SensorEvent.createMagneticRecord(referenceTimestamp: Long): MagneticRecord {
    return MagneticRecord(
        timestamp = DateTimePeriod(nanoseconds = timestamp - referenceTimestamp),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        x = values[0].toDouble(),
        y = values[1].toDouble(),
        z = values[2].toDouble()
    )
}


/**
 * @param x rot_axis.x * sin(theta/2)
 * @param y rot_axis.y * sin(theta/2)
 * @param z rot_axis.z * sin(theta/2)
 * @param w cos(theta/2)
 */
@Serializable
data class RotationRecord(
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double,
    val referenceCoordinate: String?,
    val sensorAndroidType: String?,
    val estimatedAccuracy: Double
) : SensorRecord {
    override val timestampDate: Instant? = null
    override val eventAccuracy: Int? = null
}


@ExperimentalSerializationApi
fun SensorEvent.createRotationRecord(referenceTimestamp: Long): RotationRecord {
    val sensorType = sensor.type
    val sensorAndroidType: String?
    val referenceCoordinate: String?

    if (Sensor.TYPE_ROTATION_VECTOR == sensorType) {
        sensorAndroidType = "rotationVector"
        referenceCoordinate = "East-Up-North"
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        && Sensor.TYPE_GAME_ROTATION_VECTOR == sensorType
    ) {
        sensorAndroidType = "gameRotationVector"
        referenceCoordinate = "zUp"
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        && Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR == sensorType
    ) {
        sensorAndroidType = "geomagneticRotationVector"
        referenceCoordinate = "East-Up-North"
    } else {
        sensorAndroidType = null
        referenceCoordinate = null
    }

    return RotationRecord(
        timestamp = DateTimePeriod(nanoseconds = timestamp - referenceTimestamp),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        x = values[0].toDouble(),
        y = values[1].toDouble(),
        z = values[2].toDouble(),
        w = values[3].toDouble(),
        estimatedAccuracy = values[0].toDouble(),
        sensorAndroidType = sensorAndroidType,
        referenceCoordinate = referenceCoordinate
    )
}

@Serializable
data class UncalibratedRecord(
    override val timestamp: DateTimePeriod?,
    override val sensorType: String?,
    override val uptime: Double?,
    val xUncalibrated: Double,
    val yUncalibrated: Double,
    val zUncalibrated: Double,
    val xBias: Double,
    val yBias: Double,
    val zBias: Double
) : SensorRecord {
    override val timestampDate: Instant? = null
    override val eventAccuracy: Int? = null
}

@ExperimentalSerializationApi
fun SensorEvent.createUncalibratedRecord(referenceTimestamp: Long): UncalibratedRecord {
    return UncalibratedRecord(
        timestamp = DateTimePeriod(nanoseconds = timestamp - referenceTimestamp),
        sensorType = SENSOR_TYPE_TO_DATA_TYPE[sensor.type],
        uptime = timestamp.toDouble() / SECONDS_TO_NANOS,
        xUncalibrated = values[0].toDouble(),
        yUncalibrated = values[1].toDouble(),
        zUncalibrated = values[2].toDouble(),
        xBias = values[3].toDouble(),
        yBias = values[4].toDouble(),
        zBias = values[5].toDouble()
    )
}

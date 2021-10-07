package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.recorder.SampleRecord

@Serializable
data class MotionRecord(
    override val timestampDate: Instant?,
    override val timestamp: Long?
) : SampleRecord


/// `MotionRecorderType` is used to enumerate the sensors and calculated measurements
/// that can be recorded by the `MotionRecorder`.
///
/// `MotionRecorder` records each sample from either the raw CoreMotion sensors
/// (accelerometer, gyro, and magnetometer) or the calculated vectors returned when requesting
/// `CMDeviceMotion` data updates. The `CMDeviceMotion` data is split into the components
/// enumerated by this enum into a single vector (sensor or calculated) per type.
///
/// By default, the requested types are are saved to a single logging file as instances of
/// `MotionRecord` structs.
///
/// Spliting the device motion into components in this manner stores the data in using a
/// consistent JSON schema that can represent the sensor data returned by both iOS and Android
/// devices. Thus, allowing research studies to target a broader audience. Additionally, this
/// schema allows for a single table to be used to store the data which can then be filtered
/// by type to perform calculations and DSP on the input sources.
///
@Serializable
enum class MotionRecorderType(val serializedName: String) {

    ///
    /// Raw accelerometer reading. `CMAccelerometerData` accelerometer.
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_raw_accelerometer_events
    /// See {@link android.hardware.Sensor.TYPE_ACCELEROMETER}
    @SerialName("accelerometer")
    ACCELEROMETER("accelerometer"),

    /// Raw gyroscope reading. `CMGyroData` rotationRate.
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_raw_gyroscope_events
    /// See {@link android.hardware.Sensor.TYPE_GYROSCOPE}
    @SerialName("gyro")
    GYRO("gyro"),

    /// Raw magnetometer reading. `CMMagnetometerData` magneticField.
    /// - seealso: https://developer.apple.com/documentation/coremotion/cmmagnetometerdata
    /// There isn't an android magnetometer so it maps to the magnetic field sensor.
    @SerialName("magnetometer")
    MAGNETOMETER("magnetometer"),

    /// Calculated orientation of the device using the gyro and magnetometer (if appropriate).
    ///
    /// This is included in the `CMDeviceMotion` data object.
    ///
    /// - note: If the `magneticField` is included in the configuration's list of desired
    /// recorder types then the reference frame is `.xMagneticNorthZVertical`. Otherwise,
    /// the motion recorder will use `.xArbitraryZVertical`.
    ///
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
    @SerialName("attitude")
    ATTITUDE("attitude"),

    /// Calculated vector for the direction of gravity in the coordinates of the device.
    ///
    /// This is included in the `CMDeviceMotion` data object.
    ///
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
    /// See {@link android.hardware.Sensor.TYPE_GRAVITY}
    @SerialName("gravity")
    GRAVITY("gravity"),

    /// The magnetic field vector with respect to the device for devices with a magnetometer.
    /// Note that this is the total magnetic field in the device's vicinity without device
    /// bias (Earth's magnetic field plus surrounding fields, without device bias),
    /// unlike `CMMagnetometerData` magneticField.
    ///
    /// This is included in the `CMDeviceMotion` data object.
    ///
    /// - note: If this recorder type is included in the configuration, then the attitude
    /// reference frame will be set to `.xMagneticNorthZVertical`. Otherwise, the magnetic
    /// field vector will be returned as `{ 0, 0, 0 }`.
    ///
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
    /// See {@link android.hardware.Sensor.TYPE_MAGNETIC_FIELD}
    @SerialName("magneticField")
    MAGNETIC_FIELD("magneticField"),

    /// The rotation rate of the device for devices with a gyro.
    ///
    /// This is included in the `CMDeviceMotion` data object.
    ///
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
    /// See {@link android.hardware.Sensor.TYPE_ROTATION_VECTOR}
    @SerialName("rotationRate")
    ROTATION_RATE("rotationRate"),

    /// Calculated vector for the participant's acceleration in the coordinates of the device.
    /// This is the acceleration component after subtracting the gravity vector.
    ///
    /// This is included in the `CMDeviceMotion` data object.
    ///
    /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
    /// There isn't an android user acceleration sensor so it maps to the accelerometer.
    @SerialName("userAcceleration")
    USER_ACCELERATION("userAcceleration");

    companion object {
        /// A list of all the enum values.
        val all = MotionRecorderType.values().toSet()

        /// List of the device motion types that are calculated from multiple sensors and returned
        /// by listening to device motion updates.
        ///
        /// - seealso: https://developer.apple.com/documentation/coremotion/getting_processed_device_motion_data
        val deviceMotionTypes = setOf(
            MotionRecorderType.ATTITUDE,
            MotionRecorderType.GRAVITY,
            MotionRecorderType.MAGNETIC_FIELD,
            MotionRecorderType.ROTATION_RATE,
            MotionRecorderType.USER_ACCELERATION
        )

        /// List of the raw motion sensor types.
        val rawSensorTypes = setOf(
            MotionRecorderType.ACCELEROMETER,
            MotionRecorderType.GYRO,
            MotionRecorderType.MAGNETOMETER
        )
    }
}


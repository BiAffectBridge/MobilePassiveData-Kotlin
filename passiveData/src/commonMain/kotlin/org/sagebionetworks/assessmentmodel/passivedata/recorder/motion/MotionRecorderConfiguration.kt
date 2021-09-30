package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionType

/**
 * Additional configuration parameters required by the `MotionRecorder` defined in the
 * `MotionSensor` library.
 *
 * @param recorderTypes device sensor types to include with this configuration. If null, then the `MotionRecorder` defaults will be used.
 * @param frequency The sampling frequency of the motion sensors. If null, then `MotionRecorder` defaultfrequency will be used.
 * @param usesCSVEncoding encode samples as a CSV file
 */
@Serializable
data class MotionRecorderConfiguration(
    override val identifier: String,
    override val startStepIdentifier: String? = null,
    override val stopStepIdentifier: String? = null,
    override val requiresBackgroundAudio: Boolean = false,
    override val shouldDeletePrevious: Boolean = true,
    val recorderTypes: Set<MotionRecorderType>? = null,
    val frequency: Double? = null,
    val usesCSVEncoding: Boolean = false
) : RestartableRecorderConfiguration {
    override val typeName: String
        get() = AsyncActionType.Motion.name
}


/**
 * `RecorderConfiguration` is used to configure a recorder. For example, recording accelerometer
 * data or video.
 */
interface RecorderConfiguration : AsyncActionConfiguration {
    /// An identifier marking the step at which to stop the action. If `nil`, then the action will be
    /// stopped when the task is stopped.
    val stopStepIdentifier: String?

    /// Whether or not the recorder requires background audio.
    ///
    /// If `true` then background audio can be used to keep the recorder running if the screen is locked
    /// because of the idle timer turning off the device screen.
    ///
    /// If the app uses background audio, then the developer will need to turn `ON` the "Background Modes"
    /// under the "Capabilities" tab of the Xcode project, and will need to select "Audio, AirPlay, and
    /// Picture in Picture".
    val requiresBackgroundAudio: Boolean
}

/// Extends `RecorderConfiguration` for a recorder that might be restarted.
interface RestartableRecorderConfiguration : RecorderConfiguration {

    /// Should the file used in a previous run of a recording be deleted?
    val shouldDeletePrevious: Boolean
}
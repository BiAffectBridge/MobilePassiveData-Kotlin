package org.sagebionetworks.assessmentmodel.passivedata.recorder.audio

import kotlinx.serialization.Serializable
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.RecorderConfiguration

/**
 * Default configuration to use for an AudioRecorder
 *
 * Example json:
 *  {
 *      "identifier": "foo",
 *      "type": "microphone",
 *      "startStepIdentifier": "countdown",
 *      "stopStepIdentifier": "rest",
 *      "requiresBackgroundAudio": true,
 * }
 *
 * @param saveAudioFile Should the audio recording be saved? Default = `false`. If `true` then the
 * audio file used to measure meter levels is saved with the results. Otherwise, the audio file
 * recorded is assumed to be a temporary file and should be deleted when the recording stops.
 */
@Serializable
data class AudioRecorderConfiguration(
    override val identifier: String,
    override val typeName: String,
    override val startStepIdentifier: String? = null,
    override val stopStepIdentifier: String? = null,
    override val requiresBackgroundAudio: Boolean,
    val saveAudioFile: Boolean? = false
) : RecorderConfiguration
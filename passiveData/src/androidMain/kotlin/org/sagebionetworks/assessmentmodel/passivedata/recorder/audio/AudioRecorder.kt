package org.sagebionetworks.assessmentmodel.passivedata.recorder.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.recorder.FlowJsonFileResultRecorder

class AudioRecorder(
    identifier: String,
    configuration: AsyncActionConfiguration,
    scope: CoroutineScope,
    flow: Flow<AudioLevelRecord>,
    context: Context

) : FlowJsonFileResultRecorder<AudioLevelRecord>(identifier, configuration, scope, flow, context) {
    override fun serializeElement(e: AudioLevelRecord) {
        MediaRecorder().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setAudioSource(MediaRecorder.AudioSource.UNPROCESSED)
            } else {
                setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            }
        }

        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun isPaused(): Boolean {
        TODO("Not yet implemented")
    }
}
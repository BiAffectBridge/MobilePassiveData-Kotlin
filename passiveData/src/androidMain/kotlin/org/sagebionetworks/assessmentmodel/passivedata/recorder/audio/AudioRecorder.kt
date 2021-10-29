package org.sagebionetworks.assessmentmodel.passivedata.recorder.audio

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.media.MediaRecorder
import android.os.Build
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.recorder.FlowJsonFileResultRecorder
import org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor.SensorEventComposite
import kotlin.math.ln

public class AudioRecorder(
    identifier: String,
    configuration: AsyncActionConfiguration,
    scope: CoroutineScope,
    flow: Flow<Double>,
    context: Context

) : FlowJsonFileResultRecorder<Double>(identifier, configuration, scope, flow, context) {


    override fun serializeElement(e: Double) {
        Napier.d("AudioLevel: e")

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
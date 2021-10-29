package org.sagebionetworks.assessmentmodel.passivedata.recorder.audio

import android.media.MediaRecorder
import android.os.Build
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.lang.IllegalStateException
import kotlin.math.ln


// see for more info: https://web.archive.org/web/20121225215502/http://code.google.com/p/android-labs/source/browse/trunk/NoiseAlert/src/com/google/android/noisealert/SoundMeter.java
fun AudioRecorderConfiguration.createAudioLevelFlow(): Flow<Double> {
    val flow: Flow<Double> = channelFlow {

        var mr = MediaRecorder().apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                setAudioSource(MediaRecorder.AudioSource.UNPROCESSED)
//            } else {
            setAudioSource(MediaRecorder.AudioSource.MIC)
//            }
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            setOutputFile("/dev/null");
        }

        try {
            mr.apply {
                Napier.i("Preparing MediaRecorder")
                kotlin.runCatching {
                    prepare()
                }
                Napier.i("Starting MediaRecorder")
                start()
                maxAmplitude // first call is always zero and sets up for subsequent call
            }

            while (!isClosedForSend) {
                Napier.d("Delaying")
                delay(1000)
                Napier.d("Done delaying")
                val maxAmplitude = mr.maxAmplitude
                Napier.d("Collected maxAmplitude: $maxAmplitude")
                send(20 * ln(maxAmplitude / 2700.0)) // sampled from previous call
            }
            Napier.d("Leaving Audio SamplingLoop")
        } finally {
            awaitClose {
                try {
                    Napier.i("Closing MediaRecorder")
                    mr.stop()
                    mr.release()
                } catch (e: IllegalStateException) {
                    // no-op, we are not actually recording so this is always thrown
                }
            }
        }

    }
    return flow.shareIn(
        CoroutineScope(Dispatchers.IO),
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 0, replayExpirationMillis = 0)
    )


}
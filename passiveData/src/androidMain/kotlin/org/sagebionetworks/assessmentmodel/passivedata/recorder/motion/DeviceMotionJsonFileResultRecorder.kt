package org.sagebionetworks.assessmentmodel.passivedata.recorder.motion

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.Json
import org.sagebionetworks.assessmentmodel.passivedata.asyncaction.AsyncActionConfiguration
import org.sagebionetworks.assessmentmodel.passivedata.recorder.SharedFlowJsonFileResultRecorder
import org.sagebionetworks.assessmentmodel.passivedata.recorder.motion.DeviceMotionUtil.Companion.SENSOR_TYPE_TO_RECORD_FACTORY
import android.hardware.SensorEvent
import io.github.aakira.napier.Napier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor.SensorRecord
import org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor.createFirstRecord
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.ExperimentalTime

class DeviceMotionJsonFileResultRecorder(
    override val identifier: String,
    override val configuration: AsyncActionConfiguration,
    override val scope: CoroutineScope,
    flow: SharedFlow<SensorEvent>,
    filename: String,
    context: Context,
    val jsonCoder: Json
) :
    SharedFlowJsonFileResultRecorder<SensorEvent>(
        identifier, configuration, scope, flow, filename, context
    ) {
    var firstEventUptimeReference = AtomicReference<Long>()

    @ExperimentalSerializationApi
    @ExperimentalTime
    @ExperimentalStdlibApi
    override fun serializeElement(e: SensorEvent) {
        val record: SensorRecord? = if (firstEventUptimeReference.get() == null) {
            // determine uptime reference and log full info about sensor
            val first = e.createFirstRecord()
            firstEventUptimeReference.set(e.timestamp)
            first
        } else {
            // normal log of event based on uptime reference
            SENSOR_TYPE_TO_RECORD_FACTORY[e.sensor.type]
                ?.invoke(e, firstEventUptimeReference.get())
        }

        if (record == null) {
            Napier.w("Failed to serialize SensorEvent for recorder $identifier and sensor ${e.sensor}")
        } else {
            jsonCoder.encodeToStream(record, filePrintStream)
        }
    }

    override fun pause() {
        // TODO: implement pausing - liujoshua 2021-09-12
    }

    override fun resume() {
        // TODO: implement resume - liujoshua 2021-09-12
    }

    override fun isPaused(): Boolean {
        // TODO: implement pausing - liujoshua 2021-09-12
        return false
    }
}
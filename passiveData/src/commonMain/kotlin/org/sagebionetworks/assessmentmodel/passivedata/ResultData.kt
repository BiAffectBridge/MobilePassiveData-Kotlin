@file:UseSerializers(OffsetZonedInstantSerializer::class)

package org.sagebionetworks.assessmentmodel.passivedata

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.sagebionetworks.assessmentmodel.passivedata.recorder.FileResult
import org.sagebionetworks.assessmentmodel.passivedata.recorder.weather.WeatherResult

interface ResultData {
    val identifier: String
    val startDate: Instant
    val endDate: Instant
}

val resultDataSerializersModule = SerializersModule {
    polymorphic(ResultData::class) {
        subclass(WeatherResult::class)
        subclass(FileResult::class)
    }
    contextual(Instant::class, OffsetZonedInstantSerializer)
}

object OffsetZonedInstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    private val json = Json

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        val currentZone = TimeZone.currentSystemDefault()

        val offsetInstant = value.toLocalDateTime(currentZone)
        val offsetZone = currentZone.offsetAt(value)

        encoder.encodeString(
            json.encodeToString(offsetInstant).removeSurrounding("\"")
                    + json.encodeToString(offsetZone).removeSurrounding("\"")
        )
    }

}
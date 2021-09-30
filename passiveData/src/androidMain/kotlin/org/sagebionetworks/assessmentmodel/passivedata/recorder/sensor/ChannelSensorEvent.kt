package org.sagebionetworks.assessmentmodel.passivedata.recorder.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

sealed class ChannelSensorEvent(
    private val sensorEvent: SensorEvent?,
    private val sensor: Sensor?,
    private val accuracy: Int?
) {
    @Serializable(with = EventSerializer::class)
    class Event(val sensorEvent: SensorEvent) : ChannelSensorEvent(sensorEvent, null, null)
    class AccuracyChange(val sensor: Sensor, val accuracy: Int) :
        ChannelSensorEvent(null, sensor, accuracy)

    object EventSerializer : KSerializer<Event> {
        override fun deserialize(decoder: Decoder): Event {
            TODO("Not yet implemented")
        }

        override val descriptor: SerialDescriptor
            get() = TODO("Not yet implemented")

        override fun serialize(encoder: Encoder, value: Event) {
            TODO("Not yet implemented")
        }

    }

    val serializersModule = SerializersModule {
        polymorphic(ChannelSensorEvent::class) {
            subclass(Event::class)
            subclass(AccuracyChange::class)
        }
    }
}



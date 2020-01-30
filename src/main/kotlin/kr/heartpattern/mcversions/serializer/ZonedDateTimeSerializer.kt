package kr.heartpattern.mcversions.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.time.ZonedDateTime

@Serializer(forClass = ZonedDateTime::class)
object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("ZonedDateTime")

    override fun serialize(encoder: Encoder, obj: ZonedDateTime) {
        encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        return ZonedDateTime.parse(decoder.decodeString())
    }
}
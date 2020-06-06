package io.heartpattern.mcversions.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.net.URL

object URLSerializer: KSerializer<URL>{
    override val descriptor: SerialDescriptor = StringDescriptor.withName("URL")

    override fun serialize(encoder: Encoder, obj: URL) {
        return encoder.encodeString(obj.toString())
    }

    override fun deserialize(decoder: Decoder): URL {
        return URL(decoder.decodeString())
    }
}
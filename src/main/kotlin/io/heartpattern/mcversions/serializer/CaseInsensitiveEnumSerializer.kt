package io.heartpattern.mcversions.serializer

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

open class CaseInsensitiveEnumSerializer<E : Enum<E>>(val enums: Array<E>) : KSerializer<E> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(enums.first().declaringClass.name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: E) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): E {
        val name = decoder.decodeString()
        return enums.find { it.name.equals(name, true) }
            ?: throw SerializationException("${enums.first().declaringClass.name} does not contain element with name '$name'")
    }
}
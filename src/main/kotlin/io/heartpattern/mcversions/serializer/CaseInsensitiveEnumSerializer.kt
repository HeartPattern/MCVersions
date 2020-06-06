package io.heartpattern.mcversions.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

open class CaseInsensitiveEnumSerializer<E : Enum<E>>(val enums: Array<E>) : KSerializer<E> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName(enums.first().declaringClass.simpleName)

    override fun serialize(encoder: Encoder, obj: E) {
        encoder.encodeString(obj.name)
    }

    override fun deserialize(decoder: Decoder): E {
        val name = decoder.decodeString()
        return enums.find { it.name.equals(name, true) }
            ?: throw SerializationException("${enums.first().declaringClass.name} does not contain element with name '$name'")
    }
}
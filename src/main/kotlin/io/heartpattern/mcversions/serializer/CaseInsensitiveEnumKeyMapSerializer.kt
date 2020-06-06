package io.heartpattern.mcversions.serializer

import kotlinx.serialization.*
import kotlinx.serialization.internal.NamedMapClassDescriptor
import io.heartpattern.mcversions.model.Os

object NativesSerializer: CaseInsensitiveEnumKeyMapSerializer<Os, String>(enumValues(), String.serializer())

abstract class CaseInsensitiveEnumKeyMapSerializer<K : Enum<K>, V>(
    val enums: Array<K>,
    valueSerializer: KSerializer<V>
) : KSerializer<Map<K, V>> {
    private val stringMapSerializer = (String.serializer() to valueSerializer).map
    override val descriptor: SerialDescriptor = NamedMapClassDescriptor(
        "CaseInsensitiveEnumMap",
        String.serializer().descriptor,
        valueSerializer.descriptor
    )

    override fun serialize(encoder: Encoder, obj: Map<K, V>) {
        encoder.encode(stringMapSerializer, obj.mapKeys { it.key.name })
    }

    override fun deserialize(decoder: Decoder): Map<K, V> {
        return decoder.decode(stringMapSerializer).mapKeys { (name,_) ->
            enums.find { it.name.equals(name, true) }!!
        }
    }
}
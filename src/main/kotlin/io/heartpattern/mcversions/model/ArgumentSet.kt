package io.heartpattern.mcversions.model

import io.heartpattern.mcversions.jsonFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ArgumentSet(
    val game: List<Argument>,
    val jvm: List<Argument>
)

@Serializable(Argument.Serializer::class)
interface Argument {
    val value: List<String>

    companion object Serializer : KSerializer<Argument> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("io.heartpattern.mcversions.model.Argument", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Argument) {
            when (value) {
                is StaticArgument -> encoder.encodeSerializableValue(
                    StaticArgument.serializer(), value
                )
                is RuledArgument -> encoder.encodeSerializableValue(
                    RuledArgument.serializer(), value
                )
            }
        }

        override fun deserialize(decoder: Decoder): Argument {
            val json = decoder.decodeSerializableValue(JsonElement.serializer())
            return if (json is JsonPrimitive) {
                jsonFormat.decodeFromJsonElement(StaticArgument.serializer(), json)
            } else {
                jsonFormat.decodeFromJsonElement(RuledArgument.serializer(), json)
            }
        }
    }
}

@Serializable(StaticArgument.Serializer::class)
data class StaticArgument(
    override val value: List<String>
) : Argument {
    companion object Serializer : KSerializer<StaticArgument> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("io.heartpattern.mcversions.model.StaticArgument", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: StaticArgument) {
            encoder.encodeString(value.value.first())
        }

        override fun deserialize(decoder: Decoder): StaticArgument {
            return StaticArgument(listOf(decoder.decodeString()))
        }
    }
}

@Serializable(RuledArgument.Serializer::class)
data class RuledArgument(
    override val value: List<String>,
    val rules: List<Rule>
) : Argument {
    companion object Serializer : KSerializer<RuledArgument> {
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("io.heartpattern.mversions.model.RuledArgument") {
                element<String>("value")
                element<List<String>>("rules")
            }

        override fun serialize(encoder: Encoder, value: RuledArgument) {
            if (value.value.size == 1) {
                val composite = encoder.beginStructure(descriptor)
                composite.encodeSerializableElement(descriptor, 0, ListSerializer(Rule.serializer()), value.rules)
                if (value.value.size == 1)
                    composite.encodeStringElement(descriptor, 1, value.value[0])
                else
                    composite.encodeSerializableElement(descriptor, 1, ListSerializer(String.serializer()), value.value)
            }
        }

        override fun deserialize(decoder: Decoder): RuledArgument {
            val json = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
            val rules = jsonFormat.decodeFromJsonElement(ListSerializer(Rule.serializer()), json["rules"]!!)
            val jsonValue = json["value"]!!
            val value = if (jsonValue is JsonPrimitive) {
                listOf(jsonValue.content)
            } else {
                jsonFormat.decodeFromJsonElement(ListSerializer(String.serializer()), jsonValue)
            }
            return RuledArgument(
                value,
                rules
            )
        }
    }
}
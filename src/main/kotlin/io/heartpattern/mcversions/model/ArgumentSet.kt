package io.heartpattern.mcversions.model

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.JsonElementSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import io.heartpattern.mcversions.jsonFormat

@Serializable
data class ArgumentSet(
    val game: List<Argument>,
    val jvm: List<Argument>
)

@Serializable
sealed class Argument {
    abstract val value: List<String>

    @Serializer(forClass = Argument::class)
    companion object : KSerializer<Argument> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("Argument")

        override fun serialize(encoder: Encoder, obj: Argument) {
            when (obj) {
                is StaticArgument -> encoder.encode(
                    StaticArgument.serializer(), obj)
                is RuledArgument -> encoder.encode(
                    RuledArgument.serializer(), obj)
            }
        }

        override fun deserialize(decoder: Decoder): Argument {
            val json = decoder.decode(JsonElementSerializer)
            return if (json is JsonPrimitive) {
                jsonFormat.fromJson(StaticArgument.serializer(), json)
            } else {
                jsonFormat.fromJson(RuledArgument.serializer(), json)
            }
        }
    }
}

@Serializable
data class StaticArgument(
    override val value: List<String>
) : Argument() {
    @Serializer(forClass = StaticArgument::class)
    companion object : KSerializer<StaticArgument> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("StaticArgument")

        override fun serialize(encoder: Encoder, obj: StaticArgument) {
            encoder.encodeString(obj.value.first())
        }

        override fun deserialize(decoder: Decoder): StaticArgument {
            return StaticArgument(listOf(decoder.decodeString()))
        }
    }
}

@Serializable
data class RuledArgument(
    override val value: List<String>,
    val rules: List<Rule>
) : Argument() {
    @Serializer(forClass = RuledArgument::class)
    companion object : KSerializer<RuledArgument> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("RuledArgument") {
            init {
                addElement("value")
                addElement("rules")
            }
        }

        override fun serialize(encoder: Encoder, obj: RuledArgument) {
            if (obj.value.size == 1) {
                val composit = encoder.beginStructure(descriptor)
                composit.encodeSerializableElement(descriptor, 0, Rule.serializer().list, obj.rules)
                if (obj.value.size == 1)
                    composit.encodeStringElement(descriptor, 1, obj.value[0])
                else
                    composit.encodeSerializableElement(descriptor, 1, String.serializer().list, obj.value)
            }
        }

        override fun deserialize(decoder: Decoder): RuledArgument {
            val json = decoder.decode(JsonElementSerializer) as JsonObject
            val rules = jsonFormat.fromJson(Rule.serializer().list, json["rules"]!!)
            val jsonValue = json["value"]!!
            val value = if (jsonValue is JsonPrimitive) {
                listOf(jsonValue.content)
            } else {
                jsonFormat.fromJson(String.serializer().list, jsonValue)
            }
            return RuledArgument(
                value,
                rules
            )
        }
    }
}
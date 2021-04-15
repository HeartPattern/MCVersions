package io.heartpattern.mcversions.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import io.heartpattern.mcversions.serializer.CaseInsensitiveEnumSerializer

@Serializable
data class Rule(
    val action: RuleAction,
    val os: OsRule? = null,
    val features: Map<String, Boolean> = emptyMap()
)

@Serializable(with = RuleActionSerializer::class)
enum class RuleAction {
    ALLOW, DISALLOW
}

object RuleActionSerializer : CaseInsensitiveEnumSerializer<RuleAction>(enumValues())

@Serializable
data class OsRule(
    val name: Os? = null,
    val arch: String? = null,
    val version: String? = null
)

@Serializable(with= OsSerializer::class)
enum class Os{
    OSX, LINUX, WINDOWS
}

object OsSerializer: CaseInsensitiveEnumSerializer<Os>(enumValues())
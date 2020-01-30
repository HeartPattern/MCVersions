@file:UseSerializers(URLSerializer::class, ZonedDateTimeSerializer::class)

package kr.heartpattern.mcversions.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kr.heartpattern.mcversions.serializer.CaseInsensitiveEnumSerializer
import kr.heartpattern.mcversions.serializer.URLSerializer
import kr.heartpattern.mcversions.serializer.ZonedDateTimeSerializer
import java.net.URL
import java.time.ZonedDateTime

@Serializable
data class VersionSummary(
    val id: String,
    val type: VersionType,
    val url: URL,
    val time: ZonedDateTime,
    val releaseTime: ZonedDateTime
)

@Serializable(with = VersionTypeSerializer::class)
enum class VersionType {
    SNAPSHOT,
    RELEASE,
    OLD_BETA,
    OLD_ALPHA
}

@Serializer(forClass = VersionType::class)
object VersionTypeSerializer : CaseInsensitiveEnumSerializer<VersionType>(enumValues())
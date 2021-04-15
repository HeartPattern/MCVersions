@file:UseSerializers(URLSerializer::class, ZonedDateTimeSerializer::class)

package io.heartpattern.mcversions.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import io.heartpattern.mcversions.serializer.URLSerializer
import io.heartpattern.mcversions.serializer.ZonedDateTimeSerializer
import java.net.URL
import java.time.ZonedDateTime

@Serializable
data class Version(
    val arguments: ArgumentSet? = null,
    val assetIndex: AssetIndex,
    val assets: String,
    val downloads: Downloads,
    val id: String,
    val libraries: List<Library>,
    val logging: Logging? = null,
    val mainClass: String,
    val minecraftArguments: String? = null,
    val minimumLauncherVersion: Int,
    val releaseTime: ZonedDateTime,
    val time: ZonedDateTime,
    val type: VersionType,
    val complianceLevel: Int = 0,
    val javaVersion: JavaVersion? = null
)

@Serializable
data class AssetIndex(
    val id: String,
    val sha1: String,
    val size: Long,
    val totalSize: Long,
    val url: URL
)

interface Download {
    val sha1: String
    val size: Long
    val url: URL
}

@Serializable
data class Downloads(
    val client: SimpleDownload,
    val server: SimpleDownload? = null,
    val client_mappings: SimpleDownload? = null,
    val server_mappings: SimpleDownload? = null
)

@Serializable
data class SimpleDownload(
    override val sha1: String,
    override val size: Long,
    override val url: URL
) : Download

@Serializable
data class Library(
    val name: String,
    val downloads: LibraryDownloads,
    val natives: Map<Os, String> = emptyMap(),
    val rules: List<Rule> = emptyList(),
    val extract: Extract = Extract(
        emptyList()
    )
)

@Serializable
data class LibraryDownloads(
    val artifact: LibraryDownload? = null,
    val classifiers: Map<String, LibraryDownload> = emptyMap()
)

@Serializable
data class LibraryDownload(
    val path: String,
    override val sha1: String,
    override val size: Long,
    override val url: URL
) : Download

@Serializable
data class Extract(
    val exclude: List<String>
)

@Serializable
data class Logging(
    val client: LoggingClient
)

@Serializable
data class LoggingClient(
    val argument: String,
    val file: LoggingClientDownload,
    val type: String
)

@Serializable
data class LoggingClientDownload(
    val id: String,
    override val sha1: String,
    override val size: Long,
    override val url: URL
) : Download

@Serializable
data class JavaVersion(
    val component: String,
    val majorVersion: Int
)
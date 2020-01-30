package kr.heartpattern.mcversions.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionSet(
    val latest: LatestVersionSet,
    val versions: List<VersionSummary>
)

@Serializable
data class LatestVersionSet(
    val release: String,
    val snapshot: String
)
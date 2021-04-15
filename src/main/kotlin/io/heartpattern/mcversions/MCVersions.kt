package io.heartpattern.mcversions

import io.heartpattern.mcversions.model.Version
import io.heartpattern.mcversions.model.VersionSet
import io.heartpattern.mcversions.model.VersionSummary
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.serialization.json.Json
import java.util.concurrent.CompletableFuture

internal val jsonFormat = Json {}

class MCVersions {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    fun close() {
        client.close()
    }

    fun requestVersionSetAsync(): CompletableFuture<VersionSet> {
        return client.async { requestVersionSet() }.asCompletableFuture()
    }

    suspend fun requestVersionSet(): VersionSet {
        return client.get("https://launchermeta.mojang.com/mc/game/version_manifest.json") {
            headers["Agent"] = "MCVersions"
        }
    }

    fun requestVersionAsync(version: VersionSummary): CompletableFuture<Version> {
        return client.async { requestVersion(version) }.asCompletableFuture()
    }

    suspend fun requestVersion(version: VersionSummary): Version {
        return client.get(version.url)
    }
}
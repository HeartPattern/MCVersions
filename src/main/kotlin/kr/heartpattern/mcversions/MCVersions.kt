package kr.heartpattern.mcversions

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kr.heartpattern.mcversions.model.Version
import kr.heartpattern.mcversions.model.VersionSet
import kr.heartpattern.mcversions.model.VersionSummary
import java.util.concurrent.CompletableFuture

internal val jsonFormat = Json(JsonConfiguration.Stable)

class MCVersions {
    @UseExperimental(KtorExperimentalAPI::class)
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
# MCVersions
Minecraft version information request and parsing library.

# Usage
## Kotlin
Kotlin methods support coroutine. If you does not prefer coroutine, use java's CompletableFuture methods.

```kotlin
import kotlinx.coroutines.runBlocking
import kr.heartpattern.mcversions.MCVersions

val client = MCVersions() // Create new mcversions client

runBlocking{ // Coroutine
    val versions = client.requestVersionSet() // Request version list
    val latestVersionSummary = versions.versions.first() // Get first version
    val latestVersion = client.requestVersion(latestVersionSummary) // Request detail version information
  
    println(latestVersion.id)
    println(latestVersion.releaseTime)
}
```

## Java

Java methods support java8's CompletableFuture.

```java
MCVersions client = new MCVersions(); // Create new mcversions client

CompletableFuture<VersionSet> versionsFuture = client.requestVersionSetAsync(); // Request version list
VersionSet versions = versionsFuture.get(); // Get blocking

VersionSummary latestVersionSummary = versions.getVersions().get(0); // Get first version

CompletableFuture<Version> latestVersionFuture = client.requestVersionAsync(latestVersionSummary); // Request detail version information
Version latestVersion = latestVersionFuture.get();

System.out.println(latestVersion.getId());
System.out.println(latestVersion.getReleaseTime());
```


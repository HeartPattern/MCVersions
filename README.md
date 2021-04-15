![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.heartpattern.io%2Frepository%2Fmaven-public%2Fio%2Fheartpattern%2FMCVersions%2Fmaven-metadata.xml) ![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.heartpattern.io%2Fjob%2FHeartPattern%2Fjob%2FMCVersions%2Fjob%2Fmaster%2F) ![GitHub top language](https://img.shields.io/github/languages/top/HeartPattern/MCVersions) ![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/HeartPattern/MCVersions) ![GitHub repo size](https://img.shields.io/github/repo-size/HeartPattern/MCVersions) ![GitHub](https://img.shields.io/github/license/HeartPattern/MCVersions) ![GitHub last commit](https://img.shields.io/github/last-commit/HeartPattern/MCVersions)
# MCVersions
Minecraft version information request and parsing library.

# Add to dependency
## Gradle(Kotlin DSL)
```kotlin
repositories {
    maven("https://repo.heartpattern.io/repository/maven-public/")
}

dependencies {
    implementation("io.heartpattern:mcversions:1.0.3-SNAPSHOT")
}
```

## Gradle(Groovy DSL)
```groovy
repositories {
    maven {
        url 'https://repo.heartpattern.io/repository/maven-public/'
    }
}

dependencies {
    implementation 'io.heartpattern:MCVersions:1.0.3-SNAPSHOT'
}
```

## Maven

```xml
<repositories>
	<repository>
  	<id>heartpattern</id>
    <url>https://repo.heartpattern.io/repository/maven-public/</url>
  </repository>
</repositories>

<dependencies>
	<dependency>
  	<groupId>io.heartpattern</groupId>
    <artifactId>mcversions</artifactId>
    <version>1.0.3-SNAPSHOT</version>
  </dependency>
</dependencies>
```



# Usage

## Kotlin
Kotlin methods support coroutine. If you does not prefer coroutine, use java's CompletableFuture methods.

```kotlin
import kotlinx.coroutines.runBlocking
import io.heartpattern.mcversions.MCVersions

val client = MCVersions() // Create new mcversions client

runBlocking{ // Coroutine
    val versions = client.requestVersionSet() // Request version list
    val latestVersionSummary = versions.versions.first() // Get first version
    val latestVersion = client.requestVersion(latestVersionSummary) // Request detail version information
  
    println(latestVersion.id)
    println(latestVersion.releaseTime)
}

client.close() // Close client
```

## Java

Java methods support java8's CompletableFuture.

```java
import io.heartpattern.mcversions.MCVersions;
import io.heartpattern.mcversions.model.Version;
import io.heartpattern.mcversions.model.VersionSet;
import io.heartpattern.mcversions.model.VersionSummary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class Scratch {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MCVersions client = new MCVersions();

        CompletableFuture<VersionSet> versionsFuture = client.requestVersionSetAsync();
        VersionSet versions = versionsFuture.get();

        VersionSummary latestVersionSummary = versions.getVersions().get(0);

        CompletableFuture<Version> latestVersionFuture = client.requestVersionAsync(latestVersionSummary);
        Version latestVersion = latestVersionFuture.get();

        System.out.println(latestVersion.getId());
        System.out.println(latestVersion.getReleaseTime());
        client.close();
    }
}
```


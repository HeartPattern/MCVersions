# MCVersions
Minecraft version information request and parsing library.

# Add to dependency
## Gradle(Kotlin DSL)
```kotlin
repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

dependencies {
    implementation("kr.heartpattern:MCVersions:1.0.0-SNAPSHOT")
}
```

## Gradle(Groovy DSL)
```groovy
repositories {
    maven {
        url 'https://maven.heartpattern.kr/repository/maven-public/'
    }
}

dependencies {
    implementation 'kr.heartpattern:MCVersions:1.0.0-SNAPSHOT'
}
```

## Maven

```xml
<repositories>
	<repository>
  	<id>heartpattern</id>
    <url>https://maven.heartpattern.kr/repository/maven-public/</url>
  </repository>
</repositories>

<dependencies>
	<dependency>
  	<groupId>kr.heartpattern</groupId>
    <artifactId>MCVersions</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```



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

client.close() // Close client
```

## Java

Java methods support java8's CompletableFuture.

```java
import kr.heartpattern.mcversions.MCVersions;
import kr.heartpattern.mcversions.model.Version;
import kr.heartpattern.mcversions.model.VersionSet;
import kr.heartpattern.mcversions.model.VersionSummary;

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


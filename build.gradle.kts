plugins {
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
    id("maven-publish")
}

group = "io.heartpattern"
version = "1.0.3-SNAPSHOT"

repositories {
    maven("https://repo.heartpattern.io/repository/maven-public/")
}

object Version {
    val ktor = "1.5.1"
    val kotlin = "1.4.30"
    val coroutine = "1.4.2"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutine}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Version.coroutine}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("io.ktor:ktor-client-cio:${Version.ktor}")
    implementation("io.ktor:ktor-client-serialization-jvm:${Version.ktor}")
    implementation("org.slf4j:slf4j-api:1.7.30")

    testImplementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"

}

if (project.hasProperty("nexusUser") && project.hasProperty("nexusPassword")) {
    publishing {
        publications {
            create<MavenPublication>("class") {
                artifactId = "mcversions"
                from(components.getByName("java"))
            }
        }
        repositories {
            maven(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://repo.heartpattern.io/repository/maven-public-snapshots/"
                else
                    "https://repo.heartpattern.io/repository/maven-public-releases/"
            ) {
                credentials {
                    username = properties["nexusUser"] as String
                    password = properties["nexusPassword"] as String
                }
            }
        }
    }
}
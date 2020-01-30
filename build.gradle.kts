plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.61"
    id("maven-publish")
}

group = "kr.heartpattern"
version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

val ktor_version = "1.3.0"
val kotlin_version = "1.3.60"
val coroutine_version = "1.3.0-M2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutine_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
    implementation("org.slf4j:slf4j-api:1.7.30")

    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.3")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"

}

if(project.hasProperty("nexusUser") && project.hasProperty("nexusPassword")){
    publishing {
        publications {
            create<MavenPublication>("class"){
                artifactId = "MCVersions"
                from(components.getByName("java"))
            }
        }
        repositories {
            maven (
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://maven.heartpattern.kr/repository/maven-public-snapshots/"
                else
                    "https://maven.heartpattern.kr/repository/maven-public-releases/"
            ){
                credentials {
                    username = properties["nexusUser"] as String
                    password = properties["nexusPassword"] as String
                }
            }
        }
    }
}
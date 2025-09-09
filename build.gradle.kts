plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.10"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.koog)
    implementation(libs.bundles.ktorClient)
    implementation(libs.bundles.ktorServer)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.logback)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)

    sourceSets.all {
        languageSettings {
            optIn("kotlin.uuid.ExperimentalUuidApi")
        }
    }
}

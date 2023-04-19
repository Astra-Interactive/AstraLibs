plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
    id("convention.publication")
    id("convention.library")
}

dependencies {
    // Kotlin
    compileOnly(libs.kotlinGradlePlugin)
    // Coroutines
    compileOnly(libs.coroutines.coreJvm)
    compileOnly(libs.coroutines.core)
    // Serialization
    compileOnly(libs.kotlin.serialization)
    compileOnly(libs.kotlin.serializationJson)
    compileOnly(libs.kotlin.serializationKaml)
    // Documentation
    dokkaHtmlPlugin(libs.kotlin.dokka.kotlinJavaPlugin)
    compileOnly(libs.kotlin.dokka.plugin)
    // Spigot dependencies
    compileOnly(libs.paperApi)
    compileOnly(libs.spigotApi)
    compileOnly(libs.spigot)
    compileOnly(libs.vaultapi)
    compileOnly(libs.placeholderapi)
    // Test-Core
    testImplementation(kotlin("test-junit5"))
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Test-libs
    testImplementation(libs.coroutines.core)
    testImplementation(libs.coroutines.coreJvm)
    testImplementation(libs.xerial.sqlite.jdbc)
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:2.29.0")
    // Local
    implementation(project(":ktx-core"))
}

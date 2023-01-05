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
    // Test
    testImplementation(kotlin("test"))
    testImplementation(libs.orgTesting)

    implementation(project(":ktx-core"))
    implementation(project(":spigot-core"))
}

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    compileOnly(libs.kotlin.serialization.protobuf)
    implementation(libs.bundles.klibs)
    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.gson)
    compileOnly(libs.kyori.legacy)
    compileOnly(libs.kyori.plain)
    compileOnly(libs.kyori.minimessage)
    // Test
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serializationKaml)
}

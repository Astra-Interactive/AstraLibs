plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}
dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // klibs
    implementation(libs.klibs.mikro.core)
    // Test
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    // Local
    implementation(projects.core)
}

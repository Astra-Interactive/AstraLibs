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
    compileOnly(libs.bundles.kotlin)
    // Test
    testImplementation(platform(libs.tests.junit.bom))
    testImplementation(libs.bundles.testing.libs)
    testImplementation(libs.bundles.testing.kotlin)
    // Local
    implementation(projects.ktxCore)
}
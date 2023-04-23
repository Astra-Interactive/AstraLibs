plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
    id("convention.publication")
    id("convention.library")
}
dependencies {
    compileOnly(libs.bundles.kotlin)
    // Test
    testImplementation(platform(libs.tests.junit.bom))
    testImplementation(libs.bundles.testing.libs)
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Local
    implementation(project(":ktx-core"))
}
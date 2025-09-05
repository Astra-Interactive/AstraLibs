plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    compileOnly(libs.bundles.klibs)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    // Test-Core
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    // Local
    compileOnly(projects.core)
    compileOnly(projects.coreBukkit)
}

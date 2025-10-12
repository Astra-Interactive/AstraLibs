plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // Kotlin
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.klibs.mikro.core)
    // Spigot dependencies
    compileOnly(libs.minecraft.paper.api)
    // Test-Core
    testImplementation(libs.tests.kotlin.test)
    // Local
    compileOnly(projects.core)
    compileOnly(projects.coreBukkit)
}

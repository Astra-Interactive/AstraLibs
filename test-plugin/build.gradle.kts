
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("basic-plugin")
    id("basic-shadow")
    id("basic-resource-processor")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    // Tests
    testImplementation(platform(libs.tests.junit.bom))
    testImplementation(libs.bundles.testing.libs)
    // Local
    implementation(project(":ktx-core"))
    implementation(project(":spigot-core"))
    implementation(project(":spigot-gui"))
}
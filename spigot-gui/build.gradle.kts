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
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    // Test-Core
    testImplementation(platform(libs.tests.junit.bom))
    testImplementation(libs.bundles.testing.libs)
    // Local
    implementation(project(":ktx-core"))
    implementation(project(":spigot-core"))
}

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // kdi
    compileOnly(libs.klibs.kdi)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    compileOnly("net.essentialsx:EssentialsX:2.20.1")
    // Test
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:2.29.0")
    // Local
    implementation(projects.ktxCore)
}

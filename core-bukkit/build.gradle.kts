plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // kdi
    implementation(klibs.klibs.kdi)
    implementation(klibs.klibs.mikro.core)
    implementation(klibs.klibs.kstorage)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    compileOnly(libs.minecraft.essentialsx)
    compileOnly(libs.minecraft.luckperms)
    // Test
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:3.1.0")
    testImplementation(libs.tests.mockito)
    // Local
    implementation(projects.core)
}

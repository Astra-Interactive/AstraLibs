import ru.astrainteractive.gradleplugin.setupSpigotProcessor
import ru.astrainteractive.gradleplugin.setupSpigotShadow

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("ru.astrainteractive.gradleplugin.minecraft.empty")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // kdi
    compileOnly(klibs.klibs.kdi)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    // Tests
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    // Local
    implementation(projects.ktxCore)
    implementation(projects.orm)
    implementation(projects.spigotCore)
    implementation(projects.spigotGui)
}

setupSpigotShadow(File("D:\\Minecraft Servers\\Servers\\esmp-configuration\\anarchy\\plugins"))
setupSpigotProcessor()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("basic-plugin")
    id("basic-resource-processor")
    alias(libs.plugins.shadow)
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
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
    implementation(projects.di)
}

tasks.shadowJar {
    dependencies {
        include {
            it.moduleGroup == libs.versions.project.group.get() || it.moduleGroup.contains("astralibs")
        }
    }
    archiveClassifier.set(null as String?)
    archiveBaseName.set(libs.versions.project.name.get())
    destinationDirectory.set(File(libs.versions.destionation.spigot.get()))
}

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
    testImplementation(platform(libs.tests.junit.bom))
    testImplementation(libs.bundles.testing.libs)
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
            it.moduleGroup == libs.versions.plugin.group.get() || it.moduleGroup.contains("astralibs")
        }
    }
    archiveClassifier.set(null as String?)
    archiveBaseName.set(libs.versions.plugin.name.get())
    destinationDirectory.set(File(libs.versions.destionation.spigot.get()))
}

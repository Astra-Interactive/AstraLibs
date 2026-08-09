plugins {
    alias(libs.plugins.gradle.forgegradle) apply false
    alias(libs.plugins.gradle.neoforgegradle) apply false
    alias(libs.plugins.klibs.gradle.detekt) apply false
    alias(libs.plugins.klibs.gradle.dokka)
    alias(libs.plugins.klibs.gradle.java.version) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

dependencies {
    dokka(projects.benchmarks)
    dokka(projects.command)
    dokka(projects.commandBukkit)
    dokka(projects.core)
    dokka(projects.coreBukkit)
    dokka(projects.coreForge)
    dokka(projects.coreMinecraft)
    dokka(projects.coreNeoforge)
    dokka(projects.menuBukkit)
}

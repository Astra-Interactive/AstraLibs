import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.gradle.neoforgegradle)
}

dependencies {
    // Kotlin
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.plain)
    compileOnly(libs.minecraft.kyori.minimessage)

    compileOnly(libs.minecraft.luckperms)

    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serialization.kaml)

    implementation(projects.core)
    implementation(projects.command)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(requireJinfo.jtarget.majorVersion)

dependencies {
    compileOnly(libs.minecraft.neoforgeversion)
}

configurations.runtimeElements {
    setExtendsFrom(emptySet())
}

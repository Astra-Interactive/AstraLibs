plugins {
    id("ru.astrainteractive.gradleplugin.dokka")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

dependencies {
    compileOnly(libs.kotlin.serialization.kaml)
    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.minimessage)
    compileOnly(libs.minecraft.kyori.plain)

    testImplementation(libs.kotlin.serialization.json)
    testImplementation(libs.kotlin.serialization.kaml)
    testImplementation(libs.minecraft.kyori.api)
    testImplementation(libs.minecraft.kyori.gson)
    testImplementation(libs.minecraft.kyori.legacy)
    testImplementation(libs.minecraft.kyori.minimessage)
    testImplementation(libs.minecraft.kyori.plain)
    testImplementation(libs.tests.kotlin.test)
}

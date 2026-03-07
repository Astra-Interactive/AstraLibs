plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.root.info")
}

dependencies {
    compileOnly(libs.klibs.kstorage)
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.klibs.mikro.extensions)
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.kaml)
    compileOnly(libs.kotlin.serialization.protobuf)
    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.minimessage)
    compileOnly(libs.minecraft.kyori.plain)
    compileOnly(libs.minecraft.luckperms)

    testImplementation(libs.klibs.mikro.core)
    testImplementation(libs.kotlin.coroutines.core)
    testImplementation(libs.kotlin.serialization.kaml)
    testImplementation(libs.tests.kotlin.test)
}

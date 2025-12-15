plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.klibs.mikro.extensions)
    compileOnly(libs.klibs.kstorage)

    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.protobuf)
    compileOnly(libs.kotlin.serialization.kaml)

    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.plain)
    compileOnly(libs.minecraft.kyori.minimessage)

    compileOnly(libs.minecraft.luckperms)

    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serialization.kaml)
    testImplementation(libs.kotlin.coroutines.core)
    testImplementation(libs.klibs.mikro.core)
}

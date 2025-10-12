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

    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.gson)
    compileOnly(libs.kyori.legacy)
    compileOnly(libs.kyori.plain)
    compileOnly(libs.kyori.minimessage)

    compileOnly(libs.minecraft.luckperms)

    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serialization.kaml)
}

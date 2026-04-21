import ru.astrainteractive.gradleplugin.property.util.requireJinfo

plugins {
    id("net.neoforged.gradle.userdev")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

dependencies {
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.minimessage)
    compileOnly(libs.minecraft.kyori.plain)
    compileOnly(libs.minecraft.luckperms)

    implementation(projects.command)
    implementation(projects.core)

    testImplementation(libs.kotlin.serialization.kaml)
    testImplementation(libs.tests.kotlin.test)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(requireJinfo.jtarget.majorVersion)

dependencies {
    compileOnly(libs.minecraft.neoforgeversion)
}

configurations.runtimeElements {
    setExtendsFrom(emptySet())
}

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.forgegradle)
}

dependencies {
    // Kotlin
    compileOnly(libs.kotlin.coroutines.core)

    compileOnly(libs.klibs.mikro.core)

    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.gson)
    compileOnly(libs.kyori.legacy)
    compileOnly(libs.kyori.plain)
    compileOnly(libs.kyori.minimessage)

    compileOnly(libs.minecraft.luckperms)

    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serialization.kaml)

    implementation(projects.core)
    implementation(projects.command)
}

dependencies {
    minecraft(
        "net.minecraftforge",
        "forge",
        "${libs.versions.minecraft.mojang.version.get()}-${libs.versions.minecraft.forgeversion.get()}"
    )
}

minecraft {
    mappings("official", libs.versions.minecraft.mojang.version.get())
}

configurations.runtimeElements {
    setExtendsFrom(emptySet())
}

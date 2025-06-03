plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.forgegradle)
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    implementation(libs.bundles.klibs)
    compileOnly(libs.kyori.api)
    compileOnly(libs.kyori.gson)
    compileOnly(libs.kyori.legacy)
    compileOnly(libs.kyori.plain)
    compileOnly(libs.kyori.minimessage)
    compileOnly(libs.minecraft.luckperms)
    // Test
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.kotlin.serializationKaml)
    // Local
    implementation(projects.core)
    implementation(projects.command)
}

dependencies {
    minecraft(
        "net.minecraftforge",
        "forge",
        "${libs.versions.minecraft.version.get()}-${libs.versions.minecraft.forgeversion.get()}"
    )
}

minecraft {
    mappings("official", libs.versions.minecraft.version.get())
}

configurations.runtimeElements {
    setExtendsFrom(emptySet())
}

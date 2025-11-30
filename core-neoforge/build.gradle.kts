import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.neoforgegradle)
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

tasks.withType<JavaCompile> {
    javaCompiler.set(
        javaToolchains.compilerFor {
            requireJinfo.jtarget.majorVersion
                .let(JavaLanguageVersion::of)
                .let(languageVersion::set)
        }
    )
}

dependencies {
    compileOnly(libs.minecraft.neoforgeversion)
}

configurations.runtimeElements {
    setExtendsFrom(emptySet())
}

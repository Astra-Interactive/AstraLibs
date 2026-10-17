plugins {
    id("ru.astrainteractive.gradleplugin.dokka")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.linter")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

kotlin {
    explicitApi()
}

dependencies {
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.minecraft.paper.api)

    compileOnly(projects.core)
    compileOnly(projects.coreBukkit)

    testImplementation(libs.kotlin.coroutines.core)
    testImplementation(libs.tests.kotlin.test)
}

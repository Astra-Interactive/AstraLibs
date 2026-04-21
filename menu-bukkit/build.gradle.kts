plugins {
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
    compileOnly(libs.minecraft.paper.api)

    compileOnly(projects.core)
    compileOnly(projects.coreBukkit)

    testImplementation(libs.tests.kotlin.test)
}

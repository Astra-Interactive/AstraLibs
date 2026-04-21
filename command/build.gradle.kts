plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.json)
    compileOnly(libs.minecraft.brigadier)

    implementation(projects.core)

    testImplementation(libs.tests.kotlin.test)
}

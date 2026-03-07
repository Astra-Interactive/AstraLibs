plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.root.info")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.minecraft.paper.api)

    implementation(projects.command)
    implementation(projects.core)
    implementation(projects.coreBukkit)
}

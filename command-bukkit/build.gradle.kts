plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.minecraft.paper.api)
    compileOnly(libs.kotlin.coroutines.core)

    implementation(projects.command)
    implementation(projects.core)
    implementation(projects.coreBukkit)
}

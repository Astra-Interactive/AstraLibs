plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.minecraft.paper.api)

    implementation(projects.command)
    implementation(projects.core)
}

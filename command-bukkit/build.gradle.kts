plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.minecraft.paper.api)

    implementation(projects.command)
    implementation(projects.core)
}

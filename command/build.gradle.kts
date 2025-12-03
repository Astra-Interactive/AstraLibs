plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.json)
    compileOnly("com.mojang:brigadier:1.0.500")

    testImplementation(libs.tests.kotlin.test)

    implementation(projects.core)
}

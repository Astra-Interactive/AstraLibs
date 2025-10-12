plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.json)

    testImplementation(libs.tests.kotlin.test)

    implementation(projects.core)
}

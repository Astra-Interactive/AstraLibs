plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization.json)

    testImplementation(libs.tests.kotlin.test)

    implementation(projects.core)
}

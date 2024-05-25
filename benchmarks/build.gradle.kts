import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.kotlin.benchmark)
    alias(libs.plugins.kotlin.allopen)
}

dependencies {
    // Kotlin
    implementation(libs.bundles.kotlin)
    // AstraLibs
    implementation(klibs.klibs.kdi)
    implementation(klibs.klibs.mikro.core)
    implementation(klibs.klibs.kstorage)
    // Local
    implementation(projects.core)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    // Setup configurations
    targets {
        // This one matches sourceSet name above
        register("benchmarks") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}

sourceSets {
    create("benchmarks")
}

dependencies {
    "benchmarksImplementation"("org.openjdk.jmh:jmh-core:1.21")
    "benchmarksImplementation"(libs.kotlin.benchmark.runtime)

    "benchmarksImplementation"(sourceSets.getByName("main").output + sourceSets.getByName("main").runtimeClasspath)
    "benchmarksImplementation"(libs.kotlin.serialization.protobuf)
}
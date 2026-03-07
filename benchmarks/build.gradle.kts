import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    java
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.root.info")
    alias(libs.plugins.kotlin.benchmark)
    alias(libs.plugins.kotlin.allopen)
}

dependencies {
    implementation(libs.klibs.kstorage)
    implementation(libs.klibs.mikro.core)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization.json)

    implementation(projects.core)
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("benchmarks") {
            require(this is JvmBenchmarkTarget) {
                "Only JvmBenchmarkTarget is allowed here"
            }
            jmhVersion = "1.21"
        }
    }
}

sourceSets {
    create("benchmarks")
}

dependencies {
    "benchmarksImplementation"(libs.jmh.core)
    "benchmarksImplementation"(libs.kotlin.benchmark.runtime)
    "benchmarksImplementation"(libs.kotlin.serialization.protobuf)
    sourceSets.main.invoke {
        "benchmarksImplementation"(output + runtimeClasspath)
    }
}

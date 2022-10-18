import org.jetbrains.dokka.gradle.DokkaTask
import kotlin.collections.mutableMapOf

group = Dependencies.group
version = Dependencies.version
description = "Core utilities for spigot development"

plugins {
    java
    `java-library`
    kotlin("jvm") version Dependencies.Kotlin.version apply false
    kotlin("plugin.serialization") version Dependencies.Kotlin.version apply false
    id("com.github.johnrengelman.shadow") version Dependencies.Kotlin.shadow apply true
    id("org.jetbrains.dokka") version Dependencies.Kotlin.dokka apply true
}
java {
    withJavadocJar()
    withSourcesJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    // Documentation
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.20")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
}
tasks {
    dokkaHtml.configure {
        outputDirectory.set(file("../documentation/html"))
        dokkaSourceSets {
            named("spigot-core") {
                skipDeprecated.set(false)
            }
        }
    }
    dokkaGfm.configure {
        outputDirectory.set(file("../documentation/gfm"))
    }
    dokkaJavadoc.configure {
        outputDirectory.set(file("../documentation/javadoc"))
    }
    withType<DokkaTask>().configureEach {
        pluginsMapConfiguration.set(
            mutableMapOf("org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}""")
        )
    }
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<Jar> {
        archiveClassifier.set("min")
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            this.showStandardStreams = true
        }
    }
    shadowJar {
        dependencies {
            exclude("kotlin")
        }
        isReproducibleFileOrder = true
        from(sourceSets.main.get().output)
        archiveClassifier.set(null as String?)
        exclude("kotlin")
        exclude("kotlin/")
        exclude("/kotlin")
        minimize()
    }
}
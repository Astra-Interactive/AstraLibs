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
    id("convention.publication") apply false
    id("org.jetbrains.dokka") version Dependencies.Kotlin.version apply false
//    id("com.vanniktech.maven.publish") version "0.21.0" apply false
}
//buildscript{
//    dependencies{
//        classpath("com.vanniktech:gradle-maven-publish-plugin:0.22.0")
//    }
//}
//dependencies {
//    // Documentation
//    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:${Dependencies.Kotlin.version}")
//    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:${Dependencies.Kotlin.version}")
//}
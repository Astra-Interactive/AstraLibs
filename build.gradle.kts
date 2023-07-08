group = libs.versions.project.group.get()
version = libs.versions.project.version.get()
description = libs.versions.project.description.get()

buildscript {
    dependencies {
        classpath("com.makeevrserg.gradleplugin:convention:0.0.2")
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow) apply false
}

apply(plugin = "com.makeevrserg.gradleplugin.dokka.root")
apply(plugin = "com.makeevrserg.gradleplugin.detekt")

subprojects.forEach {
    it.apply(plugin = "com.makeevrserg.gradleplugin.dokka.module")
    if (it.name != "bukkit") {
        it.apply(plugin = "com.makeevrserg.gradleplugin.publication")
    }
    it.apply(plugin = "com.makeevrserg.gradleplugin.root.info")
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "com.makeevrserg.gradleplugin.java.core")
    }
}

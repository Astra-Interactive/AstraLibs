

group = libs.versions.plugin.group.get()
version = libs.versions.plugin.version.get()
description = libs.versions.plugin.description.get()

plugins {
    java
    `java-library`
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.kotlin.dokka) apply false
    id("convention.publication") apply false
    id("detekt-convention")
}

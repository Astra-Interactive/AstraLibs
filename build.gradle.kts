buildscript {
    dependencies {
        classpath(libs.klibs.gradleplugin.convention)
        classpath(libs.klibs.gradleplugin.minecraft)
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.dokka.root")
apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
apply(plugin = "ru.astrainteractive.gradleplugin.root.info")

subprojects.forEach {
    it.apply(plugin = "ru.astrainteractive.gradleplugin.dokka.module")
    if (it.name != "bukkit") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.publication")
    }
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
}

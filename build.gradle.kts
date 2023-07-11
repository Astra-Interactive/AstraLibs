

buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:0.0.8")
        classpath("ru.astrainteractive.gradleplugin:minecraft:0.0.8")
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.dokka.root")
apply(plugin = "ru.astrainteractive.gradleplugin.detekt")

subprojects.forEach {
    it.apply(plugin = "ru.astrainteractive.gradleplugin.dokka.module")
    if (it.name != "bukkit") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.publication")
    }
    it.apply(plugin = "ru.astrainteractive.gradleplugin.root.info")
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
}

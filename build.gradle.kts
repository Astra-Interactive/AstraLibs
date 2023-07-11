

buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:0.0.10")
        classpath("ru.astrainteractive.gradleplugin:minecraft:0.0.10")
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
    it.apply(plugin = "ru.astrainteractive.gradleplugin.stub.javadoc")
    if (it.name != "bukkit") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.publication")
    }
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
}

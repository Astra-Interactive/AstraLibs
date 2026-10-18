plugins {
    id("ru.astrainteractive.gradleplugin.dokka")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.linter")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

kotlin {
    explicitApi()
}

linter {
    unitKind.set("technology")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.kotlin.serialization.json)
    compileOnly(libs.minecraft.brigadier)

    implementation(projects.core)

    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.minecraft.brigadier)
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from(layout.settingsDirectory.file("docs/command.md"))
    }
}

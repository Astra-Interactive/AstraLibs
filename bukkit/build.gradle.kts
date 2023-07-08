plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    // Kotlin
    compileOnly(libs.bundles.kotlin)
    // Spigot dependencies
    compileOnly(libs.bundles.minecraft.bukkit)
    // Tests
    testImplementation(libs.bundles.testing.kotlin)
    testImplementation(libs.tests.kotlin.test)
    // Local
    implementation(projects.ktxCore)
    implementation(projects.orm)
    implementation(projects.spigotCore)
    implementation(projects.spigotGui)
    implementation(projects.di)
}
tasks.shadowJar {
    isReproducibleFileOrder = true
    mergeServiceFiles()
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    from(sourceSets.main.get().output)
    from(project.configurations.runtimeClasspath)
    minimize()
    archiveBaseName.set(libs.versions.project.name.get())
    destinationDirectory.set(File(libs.versions.destionation.spigot.get()))
}

tasks.processResources {
    filteringCharset = "UTF-8"
    from(sourceSets.main.get().resources.srcDirs) {
        val implementations = listOf(
            libs.kotlin.coroutines.core,
            libs.kotlin.coroutines.coreJvm,
            libs.kotlin.serialization,
            libs.kotlin.serializationJson,
            libs.kotlin.serializationKaml,
            libs.kotlin.gradle,
        ).map { it.get() }
        filesMatching("plugin.yml") {
            expand(
                "main" to "${libs.versions.project.group.get()}.${libs.versions.project.name.get()}",
                "name" to libs.versions.project.name.get(),
                "prefix" to libs.versions.project.name.get(),
                "version" to libs.versions.project.version.get(),
                "description" to libs.versions.project.description.get(),
                "url" to libs.versions.project.url.get(),
                "author" to libs.versions.project.author.get(),
                "authors" to libs.versions.project.authors.get().split(";").joinToString("\",\""),
                "libraries" to implementations.joinToString("\",\""),
            )
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

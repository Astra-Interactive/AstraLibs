import org.gradle.ide.visualstudio.tasks.internal.RelativeFileNameTransformer
import org.gradle.kotlin.dsl.dependencies

plugins {
    java
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    id("com.github.johnrengelman.shadow")
}
tasks.shadowJar {
    isReproducibleFileOrder = true
    mergeServiceFiles()
//    dependencies {
//        exclude(dependency(libs.shadow))
//    }
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    from(sourceSets.main.get().output)
    from(project.configurations.runtimeClasspath)
    minimize()
    archiveBaseName.set(libs.versions.plugin.name.get())
    destinationDirectory.set(File(libs.versions.destionation.spigot.get()))
}

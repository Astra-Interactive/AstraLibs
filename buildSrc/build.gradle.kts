plugins {
    `kotlin-dsl`
    java
    `java-library`
}
repositories {
    mavenCentral()
    gradlePluginPortal()
}
val rootDirProject = file("../")

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}

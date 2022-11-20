group = Dependencies.group
version = Dependencies.version

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
    id("convention.publication")
}

dependencies {
    // Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.Kotlin.version}")
    // Coroutines
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.Kotlin.coroutines}")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Dependencies.Kotlin.coroutines}")
    // Serialization
    compileOnly("org.jetbrains.kotlin:kotlin-serialization:${Dependencies.Kotlin.version}")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.Kotlin.json}")
    compileOnly("com.charleskorn.kaml:kaml:${Dependencies.Kotlin.kaml}")
    // Documentation
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.20")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
    // Spigot dependencies
    compileOnly("io.papermc.paper:paper-api:${Dependencies.Spigot.version}")
    compileOnly("org.spigotmc:spigot-api:${Dependencies.Spigot.version}")
    compileOnly("org.spigotmc:spigot:${Dependencies.Spigot.version}")
    compileOnly("com.github.MilkBowl:VaultAPI:${Dependencies.Spigot.vault}")
    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")

    implementation(project(":ktx-core"))
}
java {
    withJavadocJar()
    withSourcesJar()
}
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/kotlin"))
        }
        resources {
            setSrcDirs(listOf("src/resources"))
        }
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
sourceSets.named("main") {
    java.srcDir("src/main/kotlin")
}

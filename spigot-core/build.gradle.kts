group = Dependencies.group
version = Dependencies.version

plugins {
    `maven-publish`
    id("signing")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
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
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks.shadowJar {
    dependencies {
        exclude("kotlin")
    }
    isReproducibleFileOrder = true
    from(sourceSets.main.get().output)
    archiveClassifier.set(null as String?)
    exclude("kotlin")
    exclude("kotlin/")
    exclude("/kotlin")
    minimize()
}


publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/Astra-Interactive/AstraLibs")
            credentials {
                val config = getConfig()
                username = config.username
                password = config.token
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            artifactId = "spigot-core"
            groupId = Dependencies.group
            version = Dependencies.version
            from(components["kotlin"])
        }
    }
}
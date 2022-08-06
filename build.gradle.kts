import org.jetbrains.dokka.gradle.DokkaTask
import kotlin.collections.mutableMapOf

object Kotlin {
    const val version = "1.7.0"
    const val coroutines = "1.6.3"
    const val json = "1.3.3"
    const val kaml = "0.46.0"
}

object Spigot {
    const val version = "1.19-R0.1-SNAPSHOT"
    const val placeholderAPI = "2.11.2"
    const val protocolLib = "4.8.0"
    const val worldGuard = "7.0.7"
    const val vault = "1.7"
    const val coreProtect = "21.2"
    const val modelEngine = "R2.5.0"
    const val essentials = "2.19.5-SNAPSHOT"
    const val discordSRV = "1.25.0"
    const val luckPerms = "5.4"
}
group = "com.astrainteractive"
version = "1.3.2"
description = "astralibs"

plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jetbrains.dokka") version "1.6.10"
}
java {
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://nexus.scarsz.me/content/groups/public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo1.maven.org/maven2/")
    maven("https://maven.playpro.com")
    maven("https://jitpack.io")
}

dependencies {
    // Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}")
    // Coroutines
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Kotlin.coroutines}")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Kotlin.coroutines}")
    // Serialization
    compileOnly("org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:${Kotlin.json}")
    compileOnly("com.charleskorn.kaml:kaml:${Kotlin.kaml}")
    // Documentation
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.20")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
    // Spigot dependencies
    compileOnly("io.papermc.paper:paper-api:${Spigot.version}")
    compileOnly("org.spigotmc:spigot-api:${Spigot.version}")
    compileOnly("org.spigotmc:spigot:${Spigot.version}")
    // Test
    testImplementation("junit:junit:4.13.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:1.24.1")
    testImplementation("io.kotest:kotest-runner-junit5:latest.release")
    testImplementation("io.kotest:kotest-assertions-core:latest.release")
    testImplementation(kotlin("test"))
}



sourceSets {
    main {
        java {
            srcDir("java")
        }
    }
}
tasks {
    dokkaHtml.configure {
        outputDirectory.set(file("../documentation/html"))
        dokkaSourceSets {
            named("main") {
                skipDeprecated.set(false)
            }
        }
    }
    dokkaGfm.configure {
        outputDirectory.set(file("../documentation/gfm"))
    }
    dokkaJavadoc.configure {
        outputDirectory.set(file("../documentation/javadoc"))
    }
    withType<DokkaTask>().configureEach {
        pluginsMapConfiguration.set(
            mutableMapOf("org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}""")
        )
    }
    withType<JavaCompile>() {
        options.encoding = "UTF-8"
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<Jar> {
        archiveClassifier.set("min")
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    test {
        useJUnit()
        testLogging {
            events("passed", "skipped", "failed")
            this.showStandardStreams = true
        }
    }
    shadowJar {
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
}

import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Properties
import java.io.FileInputStream
import kotlin.collections.mutableMapOf
var gprUser: String? = null
var gprPassword: String? = null
val kotlin_version: String by project
val kotlin_coroutines_version: String by project
val kotlin_json_version: String by project
fun getFileOrCreate(path: String): File {
    val _file = file(path)
    if (!_file.exists()) _file.createNewFile()
    return _file
}

inline fun <reified T> File.getProperty(key: String, default: T? = null): T? {
    val prop: String = Properties().apply { load(inputStream()) }.getProperty(key) ?: return default
    return when (T::class) {
        Int::class -> prop.toIntOrNull() as T?
        String::class -> prop as T?
        else -> throw IllegalStateException("Unknown Generic Type")
    }
}

inline fun <reified T> File.setProperty(key: String, value: T? = null) {
    Properties().apply {
        load(inputStream())
        this.setProperty(key, value?.toString())
    }.store(this.outputStream(), "")

}

task("Get GPR user keys") {
    val astraPropsFile = getFileOrCreate("astra.properties")
    gprUser = astraPropsFile.getProperty<String?>("gpr.user")
    gprPassword = astraPropsFile.getProperty<String?>("gpr.password")
    if (gprUser == null || gprPassword == null) {
        astraPropsFile.setProperty("gpr.user", gprUser ?: "SET_GPR_USERNAME_HERE")
        astraPropsFile.setProperty("gpr.password", gprPassword ?: "SET_GPR_KEY_HERE")
        throw GradleException("You need to set your GPR keys")
    }
}

plugins {
    java
    `maven-publish`
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://maven.pkg.github.com/Astra-Interactive/AstraLibs")
    maven("https://repo1.maven.org/maven2/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}
tasks.dokkaHtml.configure {
    outputDirectory.set(file("../documentation/html"))
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(file("../documentation/javadoc"))
}

tasks.dokkaGfm.configure {
    outputDirectory.set(file("../documentation/gfm"))
}
val dokkaHtml = tasks.dokkaHtml
tasks.withType<DokkaTask>().configureEach {
    pluginsMapConfiguration.set(
        mutableMapOf("org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}""")
    )
}

dokkaHtml.configure {
    dokkaSourceSets {
        named("main") {
            skipDeprecated.set(false)
        }
    }
}
dependencies {
    // Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    // Coroutines
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines_version")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlin_coroutines_version")
    // Serialization
    compileOnly("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_json_version")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
    compileOnly("com.charleskorn.kaml:kaml:0.45.0")
    // Documentation
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.20")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
    // Spigot dependencies
    compileOnly("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT")
    // Test
    testImplementation("junit:junit:4.13.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:1.24.1")
    testImplementation("io.kotest:kotest-runner-junit5:latest.release")
    testImplementation("io.kotest:kotest-assertions-core:latest.release")
    testImplementation(kotlin("test"))
}

group = "com.astrainteractive"
version = "1.1.8-5"
description = "astralibs"
java.sourceCompatibility = JavaVersion.VERSION_16

java {
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
sourceSets {
    main{
        java{
            srcDir("java")
        }
    }
}
tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        dependencies {
            exclude("kotlin")
        }
        isReproducibleFileOrder = true
        from(sourceSets.main.get().output)
//        from(project.configurations.runtimeClasspath)
        exclude("kotlin")
        exclude("kotlin/")
        exclude("/kotlin")
        minimize()
    }
}
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Astra-Interactive/AstraLibs")
            credentials {
                username = gprUser
                password = gprPassword
            }
        }
    }
}


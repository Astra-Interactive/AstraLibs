import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Properties
import java.io.FileInputStream
import kotlin.collections.mutableMapOf

var astraPropsFile = file("astra.properties")
if (!astraPropsFile.exists())
    astraPropsFile.createNewFile()
var astraProps = Properties().apply { load(FileInputStream(astraPropsFile)) }
val gprUser = astraProps.getProperty("gpr.user")
val gprPassword = astraProps.getProperty("gpr.password")
if (gprUser == null || gprPassword == null) {
    if (gprUser == null)
        astraProps.setProperty("gpr.user", "SET_GPR_USERNAME_HERE")
    if (gprPassword == null)
        astraProps.setProperty("gpr.password", "SET_GPR_KEY_HERE")
    astraProps.store(astraPropsFile.outputStream(), "")
    throw GradleException("You need to set your GPR keys")
}
plugins {
    java
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jetbrains.dokka") version "1.6.10"
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
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    compileOnly("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT")
    testImplementation("junit:junit:4.13.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.18:1.24.1")
    testImplementation("io.kotest:kotest-runner-junit5:latest.release")
    testImplementation("io.kotest:kotest-assertions-core:latest.release")
    testImplementation(kotlin("test"))
}

group = "com.astrainteractive"
version = "1.1.8-2"
description = "astralibs"
java.sourceCompatibility = JavaVersion.VERSION_17

java {
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
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


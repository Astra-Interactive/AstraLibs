group = Dependencies.group
version = Dependencies.version
val name = "AstraLibsShowcase"
description = "Showcase plugin from AstraInteractive"

plugins {
    `maven-publish`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow")
}

java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.Kotlin.version}")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.Kotlin.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Dependencies.Kotlin.coroutines}")
    // Serialization
    implementation("org.jetbrains.kotlin:kotlin-serialization:${Dependencies.Kotlin.version}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.Kotlin.json}")
    implementation("com.charleskorn.kaml:kaml:${Dependencies.Kotlin.kaml}")
    // Spigot dependencies
    compileOnly("io.papermc.paper:paper-api:${Dependencies.Spigot.version}")
    compileOnly("org.spigotmc:spigot-api:${Dependencies.Spigot.version}")
    compileOnly("org.spigotmc:spigot:${Dependencies.Spigot.version}")
    compileOnly("com.github.MilkBowl:VaultAPI:${Dependencies.Spigot.vault}")
    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")

    implementation(project(":ktx-core"))
    implementation(project(":spigot-core"))
}

tasks {
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
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            this.showStandardStreams = true
        }
    }
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                    "name" to project.name,
                    "version" to project.version,
                    "description" to project.description
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}
tasks.shadowJar {
    dependencies {
//        include(dependency(project(":ktx-core")))
//        include(dependency(project(":spigot-core")))
        include(dependency("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.Kotlin.version}"))
        include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.Kotlin.coroutines}"))
        include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Dependencies.Kotlin.coroutines}"))
        include(dependency("org.jetbrains.kotlin:kotlin-serialization:${Dependencies.Kotlin.version}"))
        include(dependency("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.Kotlin.json}"))
        include(dependency("com.charleskorn.kaml:kaml:${Dependencies.Kotlin.kaml}"))
    }
    isReproducibleFileOrder = true
    mergeServiceFiles()
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    from(sourceSets.main.get().output)
    from(project.configurations.runtimeClasspath)
    minimize()
    destinationDirectory.set(File("D:\\Minecraft Servers\\TEST_SERVER\\plugins"))
//    destinationDirectory.set(File("/media/makeevrserg/Новый том/Servers/Server/plugins"))
}

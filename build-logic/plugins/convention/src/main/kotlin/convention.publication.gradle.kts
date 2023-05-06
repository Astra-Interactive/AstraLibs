@file:Suppress("VariableNaming")

import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.signing
import org.gradle.kotlin.dsl.withType
import java.util.*

plugins {
    `maven-publish`
    signing
    java
    `java-library`
}

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val properties = Properties().apply {
    val secretPropsFile = project.rootProject.file("local.properties")
    if (secretPropsFile.exists()) {
        load(secretPropsFile.reader())
    }
}

val SIGNING_KEY_ID = System.getenv("SIGNING_KEY_ID") ?: properties.getProperty("signing.keyId")

val SIGNING_PASSWORD = System.getenv("SIGNING_PASSWORD") ?: properties.getProperty("signing.password")

val OSSRH_USERNAME = System.getenv("OSSRH_USERNAME") ?: properties.getProperty("ossrhUsername")

val OSSRH_PASSWORD = System.getenv("OSSRH_PASSWORD") ?: properties.getProperty("ossrhPassword")

val SIGNING_KEY = System.getenv("SIGNING_KEY") ?: properties.getProperty("signing.base64")

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
artifacts {
    archives(javadocJar)
}
java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = OSSRH_USERNAME
                password = OSSRH_PASSWORD
            }
        }
    }

    publications.create<MavenPublication>("default") {

        artifact(javadocJar.get())
        artifact(tasks["sourcesJar"])
        from(components["kotlin"])

        pom {
            name.set(project.name)
            artifactId = project.name
            group = libs.versions.plugin.group.get()
            groupId = libs.versions.plugin.group.get()
            version = libs.versions.plugin.version.get()
            description.set(libs.versions.plugin.description.get())
            url.set("https://github.com/Astra-Interactive/AstraLibs")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/Astra-Interactive/AstraLibs/blob/main/LICENSE.md")
                }
            }
            developers {
                developer {
                    id.set("makeevrserg")
                    name.set("Roman Makeev")
                    email.set("makeevrserg@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git:github.com/Astra-Interactive/AstraLibs.git")
                developerConnection.set("scm:git:ssh://github.com/Astra-Interactive/AstraLibs.git")
                url.set("https://github.com/Astra-Interactive/AstraLibs")
            }
        }
    }
}

tasks.create("Base64FromGPG") {
    val SIGNING_SECRET_KEY_RING_FILE = properties.getProperty("signing.secretKeyRingFile") ?: return@create
    val bytes = File(SIGNING_SECRET_KEY_RING_FILE).readBytes()

    val SIGNING_KEY = Base64.getEncoder().encodeToString(bytes)
    File("./key.txt").apply {
        if (!exists()) {
            createNewFile()
            writeText(SIGNING_KEY)
        }
    }
}

signing {
    useInMemoryPgpKeys(SIGNING_KEY_ID, SIGNING_KEY, SIGNING_PASSWORD)
    sign(publishing.publications)
}

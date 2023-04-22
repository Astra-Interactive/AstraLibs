import gradle.kotlin.dsl.accessors._d135a9d1b0cd444ab2257975278a515f.java

plugins {
    java
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_17
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
tasks {

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            this.showStandardStreams = true
            ignoreFailures = true
        }
    }
}
sourceSets.named("main") {
    java.srcDir("src/main/kotlin")
}

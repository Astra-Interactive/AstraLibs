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
    // Test
    testImplementation(kotlin("test"))
    testImplementation("org.testng:testng:7.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.Kotlin.coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Dependencies.Kotlin.coroutines}")
    testImplementation("org.xerial:sqlite-jdbc:3.34.0")
}

//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}
    
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
sourceSets.named("main") {
    java.srcDir("src/main/kotlin")
}

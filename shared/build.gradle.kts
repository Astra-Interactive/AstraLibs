@file:Suppress("UnusedPrivateMember")

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}
kotlin {
    val minecraftFrameworkAttribute = Attribute.of("MinecraftAttribute", String::class.java)
    jvm {
        withJava()
    }
    jvm("bukkit") {
        attributes.attribute(minecraftFrameworkAttribute, "bukkit")
    }
    jvm("velocity") {
        attributes.attribute(minecraftFrameworkAttribute, "velocity")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                api(libs.bundles.kotlin)
                // kdi
                api(libs.bundles.klibs)
            }
        }
        val jvmMain by getting
        val velocityMain by getting
        val bukkitMain by getting {
            dependencies {
                // Spigot dependencies
                compileOnly(libs.bundles.minecraft.bukkit)
                compileOnly("net.essentialsx:EssentialsX:2.20.1")
            }
        }
        val commonTest by getting {
            dependencies {
                // Test
                implementation(libs.bundles.testing.kotlin)
                implementation(libs.tests.kotlin.test)
            }
        }
        jvmMain.dependsOn(commonMain)
        velocityMain.dependsOn(jvmMain)
        bukkitMain.dependsOn(jvmMain)
    }
}

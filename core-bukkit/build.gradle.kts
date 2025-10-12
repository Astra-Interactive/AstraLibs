plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    compileOnly(libs.kotlin.coroutines.core)

    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.klibs.kstorage)

    compileOnly(libs.minecraft.vaultapi)
    compileOnly(libs.minecraft.papi)
    compileOnly(libs.minecraft.luckperms)
    compileOnly(libs.minecraft.essentialsx) {
        exclude("org.spigotmc", "spigot-api")
    }

    testImplementation(libs.tests.kotlin.test)
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:3.1.0")
    testImplementation(libs.tests.mockito)
    testImplementation(libs.klibs.kstorage)
    testImplementation(libs.kotlin.coroutines.core)

    implementation(projects.core)
}

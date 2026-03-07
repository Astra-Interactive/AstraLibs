plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.root.info")
}

dependencies {
    compileOnly(libs.klibs.kstorage)
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.minecraft.essentialsx) {
        exclude("org.spigotmc", "spigot-api")
    }
    compileOnly(libs.minecraft.luckperms)
    compileOnly(libs.minecraft.papi)
    compileOnly(libs.minecraft.vaultapi)

    implementation(projects.core)

    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:3.1.0")
    testImplementation(libs.klibs.kstorage)
    testImplementation(libs.kotlin.coroutines.core)
    testImplementation(libs.tests.kotlin.test)
    testImplementation(libs.tests.mockito)
}

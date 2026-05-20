plugins {
    id("net.minecraftforge.gradle")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("ru.astrainteractive.gradleplugin.detekt")
    id("ru.astrainteractive.gradleplugin.java.version")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

repositories {
    minecraft.mavenizer(this)
    mavenCentral()
    mavenLocal()
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
}

dependencies {
    compileOnly(libs.klibs.mikro.core)
    compileOnly(libs.kotlin.coroutines.core)
    compileOnly(libs.minecraft.kyori.api)
    compileOnly(libs.minecraft.kyori.gson)
    compileOnly(libs.minecraft.kyori.legacy)
    compileOnly(libs.minecraft.kyori.minimessage)
    compileOnly(libs.minecraft.kyori.plain)
    compileOnly(libs.minecraft.luckperms)

    implementation(projects.command)
    implementation(projects.core)

    api(projects.coreMinecraft)

    testImplementation(libs.kotlin.serialization.kaml)
    testImplementation(libs.tests.kotlin.test)
}

dependencies {
    compileOnly(minecraft.dependency(libs.minecraft.forgeversion.get()))
}

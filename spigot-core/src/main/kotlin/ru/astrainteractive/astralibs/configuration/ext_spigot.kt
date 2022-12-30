package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

internal fun FileConfiguration.stringConfigurationInit(path: String, default: String): String =
    this.getString(path, default) ?: default

fun FileConfiguration.stringConfiguration(path: String, default: String): Configuration<String> = configuration(path) {
    stringConfigurationInit(path, default)
}
fun FileConfiguration.mutableStringConfiguration(path: String, default: String): MutableConfiguration<String> =
    mutableConfiguration(path, init = {
        stringConfigurationInit(path, default)
    }, onUpdate = {
        this.set(path, it)
    })


internal fun FileConfiguration.intConfigurationInit(path: String, default: Int) = this.getInt(path, default)
fun FileConfiguration.intConfiguration(path: String, default: Int): Configuration<Int> = configuration(path) {
    intConfigurationInit(path, default)
}

fun FileConfiguration.mutableIntConfiguration(path: String, default: Int): MutableConfiguration<Int> =
    mutableConfiguration(path, init = {
        intConfigurationInit(path, default)
    }, onUpdate = {
        this.set(path, it)
    })
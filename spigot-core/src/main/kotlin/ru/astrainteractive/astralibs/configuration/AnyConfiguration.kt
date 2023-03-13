package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.api.MutableConfiguration

inline fun <reified T> AnyConfiguration(
    fileConfiguration: FileConfiguration,
    path: String,
    default: T
) = object : MutableConfiguration<T> by DefaultConfiguration(
    default = default,
    save = { fileConfiguration.set(path, value) },
    load = { fileConfiguration.get(path, default) as? T ?: default }
) {}
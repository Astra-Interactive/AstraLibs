package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

class IntConfiguration(
    fileConfiguration: FileConfiguration,
    path: String,
    default: Int
) : MutableConfiguration<Int> by DefaultConfiguration(
    default = default,
    save = { fileConfiguration.set(path, value) },
    load = { fileConfiguration.getInt(path, default) }
)

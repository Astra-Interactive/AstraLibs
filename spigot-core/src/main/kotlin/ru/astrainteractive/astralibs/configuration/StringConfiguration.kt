package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

sealed interface StringConfiguration {
    class Nullable(
        fileConfiguration: FileConfiguration,
        path: String,
        default: String
    ) : MutableConfiguration<String> by DefaultConfiguration(
        default = default,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path, default) ?: default }
    )

    class Optional(
        fileConfiguration: FileConfiguration,
        path: String,
    ) : MutableConfiguration<String?> by DefaultConfiguration(
        default = null,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path) }
    )

}
package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

sealed interface StringConfiguration {
    class Nullable(
        fileConfiguration: FileConfiguration,
        path: String,
        default: String
    ) : MutableConfiguration<String> by BukkitConfiguration(
        fileConfiguration = fileConfiguration,
        path = path,
        default = default,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path, default) ?: default }
    )

    class Optional(
        fileConfiguration: FileConfiguration,
        path: String,
    ) : MutableConfiguration<String?> by BukkitConfiguration(
        fileConfiguration = fileConfiguration,
        path = path,
        default = null,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path) }
    )

}
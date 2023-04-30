package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.api.MutableConfiguration

sealed interface StringConfiguration {
    class Default(
        fileConfiguration: FileConfiguration,
        path: String,
        default: String
    ) : MutableConfiguration<String> by DefaultConfiguration(
        default = default,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path, default) ?: default }
    )

    class Nullable(
        fileConfiguration: FileConfiguration,
        path: String,
    ) : MutableConfiguration<String?> by DefaultConfiguration(
        default = null,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getString(path) }
    )
}

package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.configuration.api.MutableConfiguration

sealed interface IntConfiguration {
    class Default(
        fileConfiguration: FileConfiguration,
        path: String,
        default: Int
    ) : MutableConfiguration<Int> by DefaultConfiguration(
        default = default,
        save = { fileConfiguration.set(path, value) },
        load = { fileConfiguration.getInt(path, default) }
    )

    class Nullable(
        fileConfiguration: FileConfiguration,
        path: String,
    ) : MutableConfiguration<Int?> by DefaultConfiguration(
        default = null,
        save = { fileConfiguration.set(path, value) },
        load = { if (fileConfiguration.contains(path)) fileConfiguration.getInt(path) else null }
    )
}

package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kstorage.MutableStorageValue

@Suppress("FunctionNaming")
inline fun <reified T> AnyFileConfiguration(
    fileConfiguration: FileConfiguration,
    path: String,
    default: T
) = MutableStorageValue(
    default = default,
    saveSettingsValue = { fileConfiguration.set(path, it) },
    loadSettingsValue = { fileConfiguration.get(path, default) as? T ?: default }
)

package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration

class StringListConfiguration(
    fileConfiguration: FileConfiguration,
    path: String,
) : MutableConfiguration<List<String>> by DefaultConfiguration(
    default = emptyList(),
    save = { fileConfiguration.set(path, value) },
    load = { fileConfiguration.getStringList(path) }
)

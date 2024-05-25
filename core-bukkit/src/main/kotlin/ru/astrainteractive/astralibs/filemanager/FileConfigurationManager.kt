package ru.astrainteractive.astralibs.filemanager

import org.bukkit.configuration.file.FileConfiguration

/**
 * This [FileConfigurationManager] designed for spigot
 * It contains [fileConfiguration] which can be reloaded via [load] and saved via [save]
 */
interface FileConfigurationManager : ResourceFileManager {
    /**
     * File configuration of file [FileManager.configFile]
     */
    val fileConfiguration: FileConfiguration

    /**
     * This function will save current configuration in [fileConfiguration] into [FileManager.configFile]
     */
    fun save()

    /**
     * This function will load configuration from [FileManager.configFile] into [fileConfiguration]
     */
    fun load()
}

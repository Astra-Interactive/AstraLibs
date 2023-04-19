package ru.astrainteractive.astralibs.file_manager

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.filemanager.FileManager
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import java.io.File

/**
 * This [SpigotFileManager] designed for spigot
 * It contains [fileConfiguration] which can be reloaded via [reload] and saved via [save]
 */
interface SpigotFileManager : ResourceFileManager {
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
    fun reload()

    companion object {
        operator fun invoke(
            name: String,
            dataFolder: File = AstraLibs.instance.dataFolder,
        ): SpigotFileManager = SpigotFileManagerImpl(
            name = name,
            dataFolder = dataFolder
        )
    }
}
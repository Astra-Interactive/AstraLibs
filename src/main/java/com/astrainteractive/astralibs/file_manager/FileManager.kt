package com.astrainteractive.astralibs.file_manager

import com.astrainteractive.astralibs.AstraLibs
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * File manager for every single file
 * You can create new files, change them, save/load them
 * If file not exist in resouces, it will be created anyway
 * @param configName is name of the file with file type
 */
public class FileManager(
    val configName: String,
    val dataFolder: File = AstraLibs.instance.dataFolder,
    val resourceProvider: IResourceProvider = DefaultResourceProvider(dataFolder)
) {
    /**
     * Reference for the file
     */
    var configFile: File = loadConfigFile()
        private set

    /**
     * Reference for file configuration
     */
    var fileConfiguration: FileConfiguration = loadFileConfiguration()
        private set

    /**
     * Initialization of file
     */
    private fun loadConfigFile(): File {
        var file = File(dataFolder, configName)
        if (file.exists()) return file
        if (resourceProvider.isResourceExists(configName))
            return resourceProvider.saveFromResource(configName)
        file = File(dataFolder, configName)
        file.parentFile?.mkdirs()
        file.createNewFile()
        return file
    }

    private fun loadFileConfiguration(): YamlConfiguration {
        val fileConfiguration = YamlConfiguration.loadConfiguration(configFile)
        val defaultConfig = YamlConfiguration.loadConfiguration(configFile)
        fileConfiguration.setDefaults(defaultConfig)
        return fileConfiguration
    }

    fun save() {
        fileConfiguration.save(configFile)
    }

    fun reload() {
        configFile = loadConfigFile()
        fileConfiguration = loadFileConfiguration()
    }

}
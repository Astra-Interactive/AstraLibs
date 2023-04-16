package ru.astrainteractive.astralibs.file_manager

import ru.astrainteractive.astralibs.AstraLibs
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import ru.astrainteractive.astralibs.filemanager.FileManager
import java.io.File


/**
 * File manager for every single file
 * You can create new files, change them, save/load them
 * If file not exist in resouces, it will be created anyway
 * @param name is name of the file with file type
 */
internal class SpigotFileManagerImpl(
    override val name: String,
    override val dataFolder: File = AstraLibs.instance.dataFolder,
) : SpigotFileManager {
    /**
     * Reference for the file
     */
    override var configFile: File = loadConfigFile()

    /**
     * Reference for file configuration
     */
    override var fileConfiguration: FileConfiguration = loadFileConfiguration()
        private set

    override val isResourceExists = AstraLibs.instance.getResource(name) != null
    override fun loadFromResource(): File {
        AstraLibs.instance.saveResource(name, false)
        return File(dataFolder, name)
    }

    /**
     * Initialization of file
     */
    override fun loadConfigFile(): File {
        var file = File(dataFolder, name)
        if (file.exists())
            return file
        if (isResourceExists)
            return loadFromResource()
        file = File(dataFolder, name)
        file.parentFile?.mkdirs()
        file.createNewFile()
        return file
    }

    override fun loadFileConfiguration(): FileConfiguration {
        val fileConfiguration = YamlConfiguration.loadConfiguration(configFile)
        val defaultConfig = YamlConfiguration.loadConfiguration(configFile)
        fileConfiguration.setDefaults(defaultConfig)
        return fileConfiguration
    }

    override fun save() {
        fileConfiguration.save(configFile)
    }

    override fun reload() {
        this.configFile = loadConfigFile()
        this.fileConfiguration = loadFileConfiguration()
    }
}
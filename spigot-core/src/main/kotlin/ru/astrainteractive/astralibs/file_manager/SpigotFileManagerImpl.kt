package ru.astrainteractive.astralibs.file_manager

import ru.astrainteractive.astralibs.AstraLibs
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import ru.astrainteractive.astralibs.filemanager.FileManager
import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
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
    override var configFile: File = loadConfigFile()

    override var fileConfiguration: FileConfiguration = loadFileConfiguration()
        private set

    override val isResourceExists = AstraLibs.instance.getResource(name) != null
    private fun loadFromResource(): File {
        AstraLibs.instance.saveResource(name, true)
        return File(dataFolder, name)
    }

    private fun loadConfigFile(): File {
        val file = File(dataFolder, name)
        if (file.exists()) return file
        if (!isResourceExists) throw ResourceFileManager.Exception.ResourceNotExists(name)
        return loadFromResource()
    }

    private fun loadFileConfiguration(): FileConfiguration {
        val fileConfiguration = YamlConfiguration.loadConfiguration(configFile)
        val defaultConfig = YamlConfiguration.loadConfiguration(configFile)
        fileConfiguration.setDefaults(defaultConfig)
        return fileConfiguration
    }

    override fun save() {
        fileConfiguration.save(configFile)
    }

    override fun delete() {
        configFile.delete()
        fileConfiguration.load(configFile)
    }

    override fun reload() {
        this.configFile = loadConfigFile()
        this.fileConfiguration = loadFileConfiguration()
    }
}
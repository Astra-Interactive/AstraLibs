package ru.astrainteractive.astralibs.filemanager

import ru.astrainteractive.astralibs.AstraLibs
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File


/**
 * File manager for every single file
 * You can create new files, change them, save/load them
 * If file not exist in resouces, it will be created anyway
 * @param name is name of the file with file type
 * @param isOptional - is config file placed from .jar is optional
 */
internal class SpigotFileManagerImpl(
    override val name: String,
    override val dataFolder: File = AstraLibs.instance.dataFolder,
    val isOptional: Boolean = false
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
        return if (!isResourceExists && !isOptional) throw ResourceFileManager.Exception.ResourceNotExists(name)
        else if (isResourceExists) loadFromResource()
        else File(dataFolder, name).also {
            it.parentFile.mkdirs()
            it.createNewFile()
        }
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
package ru.astrainteractive.astralibs.filemanager

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import ru.astrainteractive.klibs.kdi.Reloadable
import ru.astrainteractive.klibs.kdi.getValue
import java.io.File

/**
 * File manager for every single file
 * You can create new files, change them, save/load them
 * If file not exist in resouces, it will be created anyway
 * @param name is name of the file with file type
 */
class DefaultFileConfigurationManager(
    private val plugin: Plugin,
    override val name: String,
    override val dataFolder: File = plugin.dataFolder,
    private val isResourceRequired: Boolean = false
) : FileConfigurationManager {

    override val isResourceExists: Boolean
        get() = plugin.getResource(name) != null

    override val configFile: File
        get() {
            val file = File(dataFolder, name)
            if (file.exists()) return file
            if (!isResourceExists && isResourceRequired) throw ResourceFileManager.Exception.ResourceNotExists(name)
            if (isResourceRequired) {
                plugin.saveResource(name, true)
            } else {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            return file
        }

    private val reloadableFileConfiguration = Reloadable {
        val fileConfiguration = YamlConfiguration.loadConfiguration(configFile)
        val defaultConfig = YamlConfiguration.loadConfiguration(configFile)
        fileConfiguration.setDefaults(defaultConfig)
        fileConfiguration
    }

    override val fileConfiguration: FileConfiguration by reloadableFileConfiguration

    override fun save() {
        fileConfiguration.save(configFile)
    }

    override fun delete() {
        configFile.delete()
        fileConfiguration.load(configFile)
    }

    override fun load() {
        reloadableFileConfiguration.reload()
    }
}
